package util;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import javax.persistence.Column;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.lang.reflect.Modifier;

public class ExcelIO {

	public static <T> List<T> readExcelData(String fileName, Class<T> clazz) {
		List<T> dataList = new ArrayList<>();

		try (FileInputStream fis = new FileInputStream(fileName); Workbook workbook = createWorkbook(fileName, fis)) {

			Sheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				throw new IllegalStateException("Excel file has no sheets");
			}

			// Get header row and create column mapping
			Row headerRow = sheet.getRow(0);
			if (headerRow == null) {
				throw new IllegalStateException("Excel file has no header row");
			}

			// Get all fields including inherited ones
			List<Field> allFields = getAllFields(clazz);
			Map<Integer, Field> columnToFieldMap = new HashMap<>();
			Map<String, Integer> headerToColumnMap = new HashMap<>();

			// Create mapping of header names to column indices
			for (int i = 0; i < headerRow.getLastCellNum(); i++) {
				Cell cell = headerRow.getCell(i);
				if (cell != null) {
					String headerName = cell.getStringCellValue().trim().toUpperCase();
					headerToColumnMap.put(headerName, i);
				}
			}

			// Map columns to fields
			for (Field field : allFields) {
				field.setAccessible(true);
				if (shouldSkipField(field)) {
					continue;
				}

				String columnName = getColumnName(field).toUpperCase();
				Integer columnIndex = headerToColumnMap.get(columnName);
				if (columnIndex != null) {
					columnToFieldMap.put(columnIndex, field);
				}
			}

			// Process data rows
			for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
				Row row = sheet.getRow(rowNum);
				if (row == null || isRowEmpty(row)) {
					continue;
				}

				T instance = clazz.getDeclaredConstructor().newInstance();

				// Process each mapped column
				for (Map.Entry<Integer, Field> entry : columnToFieldMap.entrySet()) {
					int columnIndex = entry.getKey();
					Field field = entry.getValue();
					Cell cell = row.getCell(columnIndex);

					if (cell != null) {
						Object value = getCellValue(cell, field.getType());
						field.set(instance, value);
					}
				}

				dataList.add(instance);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error reading Excel file: " + e.getMessage(), e);
		}

		return dataList;
	}

	private static Workbook createWorkbook(String fileName, FileInputStream fis) throws IOException {
	    if (fileName.toLowerCase().endsWith(".xlsx")) {
	        return new XSSFWorkbook(fis);
	    } else if (fileName.toLowerCase().endsWith(".xls")) {
	        return new HSSFWorkbook(fis);
	    }
	    throw new IllegalArgumentException("Not an Excel file: " + fileName);
	}

	private static List<Field> getAllFields(Class<?> clazz) {
	    List<Field> fields = new ArrayList<>();
	    Class<?> currentClass = clazz;
	    
	    while (currentClass != null) {
	        fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
	        currentClass = currentClass.getSuperclass();
	    }
	    
	    return fields;
	}

	private static boolean shouldSkipField(Field field) {
	    int modifiers = field.getModifiers();
	    return Modifier.isStatic(modifiers) || 
	           Modifier.isFinal(modifiers) || 
	           Modifier.isTransient(modifiers);
	}

	private static String getColumnName(Field field) {
	    Column column = field.getAnnotation(Column.class);
	    if (column != null && !column.name().isEmpty()) {
	        return column.name();
	    }
	    return field.getName();
	}

	private static boolean isRowEmpty(Row row) {
	    if (row == null) {
	        return true;
	    }
	    for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
	        Cell cell = row.getCell(i);
	        if (cell != null && cell.getCellType() != CellType.BLANK) {
	            return false;
	        }
	    }
	    return true;
	}

	private static Object getCellValue(Cell cell, Class<?> targetType) {
	    if (cell == null) {
	        return targetType.isPrimitive() ? getDefaultValue(targetType) : null;
	    }

	    try {
	        switch (cell.getCellType()) {
	            case BLANK:
	                return targetType.isPrimitive() ? getDefaultValue(targetType) : null;
	            case BOOLEAN:
	                boolean boolValue = cell.getBooleanCellValue();
	                if (targetType == Boolean.class || targetType == boolean.class) {
	                    return boolValue;
	                }
	                return String.valueOf(boolValue);
	            case NUMERIC:
	                if (DateUtil.isCellDateFormatted(cell)) {
	                    Date date = cell.getDateCellValue();
	                    if (targetType == Date.class) {
	                        return date;
	                    } else if (targetType == LocalDate.class) {
	                        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	                    } else if (targetType == LocalDateTime.class) {
	                        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	                    }
	                    return date.toString();
	                }
	                
	                double numericValue = cell.getNumericCellValue();
	                if (targetType == Double.class || targetType == double.class) {
	                    return numericValue;
	                } else if (targetType == Float.class || targetType == float.class) {
	                    return (float) numericValue;
	                } else if (targetType == Long.class || targetType == long.class) {
	                    return (long) numericValue;
	                } else if (targetType == Integer.class || targetType == int.class) {
	                    return (int) numericValue;
	                } else if (targetType == BigDecimal.class) {
	                    return BigDecimal.valueOf(numericValue);
	                }
	                return numericValue;
	            case STRING:
	                String stringValue = cell.getStringCellValue().trim();
	                if (stringValue.isEmpty() || stringValue.equalsIgnoreCase("null")) {
	                    return targetType.isPrimitive() ? getDefaultValue(targetType) : null;
	                }
	                if (targetType == String.class) {
	                    return stringValue;
	                }
	                try {
	                    if (targetType == Double.class || targetType == double.class) {
	                        return Double.parseDouble(stringValue);
	                    } else if (targetType == Float.class || targetType == float.class) {
	                        return Float.parseFloat(stringValue);
	                    } else if (targetType == Long.class || targetType == long.class) {
	                        return Long.parseLong(stringValue);
	                    } else if (targetType == Integer.class || targetType == int.class) {
	                        return Integer.parseInt(stringValue);
	                    } else if (targetType == BigDecimal.class) {
	                        return new BigDecimal(stringValue);
	                    }
	                } catch (NumberFormatException e) {
	                    return targetType.isPrimitive() ? getDefaultValue(targetType) : null;
	                }
	                return stringValue;
	            default:
	                return targetType.isPrimitive() ? getDefaultValue(targetType) : null;
	        }
	    } catch (Exception e) {
	        return targetType.isPrimitive() ? getDefaultValue(targetType) : null;
	    }
	}

	private static Object getDefaultValue(Class<?> targetType) {
	    if (targetType == boolean.class) return false;
	    if (targetType == byte.class) return (byte) 0;
	    if (targetType == short.class) return (short) 0;
	    if (targetType == int.class) return 0;
	    if (targetType == long.class) return 0L;
	    if (targetType == float.class) return 0.0f;
	    if (targetType == double.class) return 0.0d;
	    if (targetType == char.class) return '\u0000';
	    return null;
	}

	public static <T> void writeListToFile(String fileName, List<T> dataList, Class<T> clazz) throws Exception {
		if (dataList == null || dataList.isEmpty()) {
			throw new IllegalArgumentException("Data list cannot be null or empty");
		}

		try (Workbook workbook = createWorkbook(fileName)) {
			Sheet sheet = workbook.createSheet(clazz.getSimpleName());

			// Get all fields including inherited ones
			List<Field> allFields = getAllFields(clazz);

			// Create header row with field names
			Row headerRow = sheet.createRow(0);
			Map<Integer, Field> columnMapping = new HashMap<>();
			int columnIndex = 0;

			for (Field field : allFields) {
				field.setAccessible(true);

				// Skip certain fields we don't want to export
				if (shouldSkipField(field)) {
					continue;
				}

				// Get the column name (either from @Column annotation or field name)
				String columnName = getColumnName(field);
				Cell cell = headerRow.createCell(columnIndex);
				cell.setCellValue(columnName);

				columnMapping.put(columnIndex, field);
				columnIndex++;
			}

			// Write data rows
			int rowIndex = 1;
			for (T data : dataList) {
				Row row = sheet.createRow(rowIndex++);

				for (int i = 0; i < columnMapping.size(); i++) {
					Field field = columnMapping.get(i);
					Object value = field.get(data);
					Cell cell = row.createCell(i);
					setCellValue(cell, value);
				}
			}

			// Auto-size columns
			for (int i = 0; i < columnMapping.size(); i++) {
				sheet.autoSizeColumn(i);
			}

			// Write to file
			try (FileOutputStream fos = new FileOutputStream(fileName)) {
				workbook.write(fos);
			}
		}
	}

	private static Workbook createWorkbook(String fileName) throws IOException {
		if (fileName.toLowerCase().endsWith("xlsx")) {
			return new XSSFWorkbook();
		} else if (fileName.toLowerCase().endsWith("xls")) {
			return new HSSFWorkbook();
		} else {
			throw new IllegalArgumentException("Invalid file format. Use .xlsx or .xls");
		}
	}

	private static String camelToSnake(String str) {
		String regex = "([a-z])([A-Z])";
		String replacement = "$1_$2";
		return str.replaceAll(regex, replacement).toLowerCase();
	}

	private static void setCellValue(Cell cell, Object value) {
		if (value == null) {
			cell.setCellValue("");
			return;
		}

		if (value instanceof String) {
			cell.setCellValue((String) value);
		} else if (value instanceof Number) {
			cell.setCellValue(((Number) value).doubleValue());
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof Date) {
			cell.setCellValue((Date) value);
			CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();
			CreationHelper createHelper = cell.getSheet().getWorkbook().getCreationHelper();
			cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd"));
			cell.setCellStyle(cellStyle);
		} else {
			cell.setCellValue(value.toString());
		}
	}

}