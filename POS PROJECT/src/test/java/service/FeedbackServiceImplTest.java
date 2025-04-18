package service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import dao.FeedbackDao;
import model.Feedback;
import service.FeedbackServiceImpl;

import java.util.Arrays;
import java.util.List;

public class FeedbackServiceImplTest {
	 private FeedbackDao mockFeedbackDao;

	    private FeedbackServiceImpl feedbackService;

	    @BeforeEach
	    public void setUp() {
	    	mockFeedbackDao = mock(FeedbackDao.class);
	        feedbackService = new FeedbackServiceImpl(mockFeedbackDao);
	    }

	    @Test
	    public void testCreateFeedback() throws Exception {
	        Feedback feedback = new Feedback();
	        when(mockFeedbackDao.create(feedback)).thenReturn(true);

	        boolean result = feedbackService.createFeedback(feedback);

	        assertTrue(result);
	        verify(mockFeedbackDao).create(feedback);
	    }
	    
	    @Test
	    public void testUpdateFeedback() throws Exception {
	        Feedback feedback = new Feedback();
	        when(mockFeedbackDao.update(feedback)).thenReturn(true);

	        boolean result = feedbackService.updateFeedback(feedback);

	        assertTrue(result);
	        verify(mockFeedbackDao).update(feedback);
	    }

	    @Test
	    public void testDeleteFeedbackById() throws Exception {
	        Long feedbackId = 1L;
	        when(mockFeedbackDao.deleteById(feedbackId)).thenReturn(true);

	        boolean result = feedbackService.deleteFeedbackById(feedbackId);

	        assertTrue(result);
	        verify(mockFeedbackDao).deleteById(feedbackId);
	    }

	    @Test
	    public void testDeleteFeedback() throws Exception {
	        Feedback feedback = new Feedback();
	        when(mockFeedbackDao.delete(feedback)).thenReturn(true);

	        boolean result = feedbackService.deleteFeedback(feedback);

	        assertTrue(result);
	        verify(mockFeedbackDao).delete(feedback);
	    }

	    @Test
	    public void testFindFeedbackById() throws Exception {
	        Long feedbackId = 1L;
	        Feedback feedback = new Feedback();
	        when(mockFeedbackDao.findById(feedbackId)).thenReturn(feedback);

	        Feedback result = feedbackService.findFeedbackById(feedbackId);

	        assertNotNull(result);
	        assertEquals(feedback, result);
	        verify(mockFeedbackDao).findById(feedbackId);
	    }

	    @Test
	    public void testFindAllFeedback() throws Exception {
	        List<Feedback> feedbacks = Arrays.asList(new Feedback(), new Feedback());
	        when(mockFeedbackDao.findAll()).thenReturn(feedbacks);

	        List<Feedback> result = feedbackService.findAllFeedback();

	        assertNotNull(result);
	        assertEquals(2, result.size());
	        verify(mockFeedbackDao).findAll();
	    }
	    @Test
	    public void testFindFeedbackByUserId() throws Exception {
	        Long personId = 1L;
	        List<Feedback> feedbacks = Arrays.asList(new Feedback(), new Feedback());
	        when(mockFeedbackDao.findByPersonId(personId)).thenReturn(feedbacks);

	        List<Feedback> result = feedbackService.findFeedbackByPersonId(personId);

	        assertNotNull(result);
	        assertEquals(2, result.size());
	        verify(mockFeedbackDao).findByPersonId(personId);
	    }

	    @Test
	    public void testFindFeedbackByProductId() throws Exception {
	        Long productId = 1L;
	        List<Feedback> feedbacks = Arrays.asList(new Feedback(), new Feedback());
	        when(mockFeedbackDao.findByProductId(productId)).thenReturn(feedbacks);

	        List<Feedback> result = feedbackService.findFeedbackByProductId(productId);

	        assertNotNull(result);
	        assertEquals(2, result.size());
	        verify(mockFeedbackDao).findByProductId(productId);
	    }

	    // Kiểm tra các trường hợp ngoại lệ
	    @Test
	    public void testCreateFeedbackException() throws Exception {
	        Feedback feedback = new Feedback();
	        when(mockFeedbackDao.create(feedback)).thenThrow(new RuntimeException("Database error"));

	        boolean result = feedbackService.createFeedback(feedback);

	        assertFalse(result);
	        verify(mockFeedbackDao).create(feedback);
	    }

	    @Test
	    public void testUpdateFeedbackException() throws Exception {
	        Feedback feedback = new Feedback();
	        when(mockFeedbackDao.update(feedback)).thenThrow(new RuntimeException("Database error"));

	        boolean result = feedbackService.updateFeedback(feedback);

	        assertFalse(result);
	        verify(mockFeedbackDao).update(feedback);
	    }

	    @Test
	    public void testDeleteFeedbackByIdException() throws Exception {
	        Long feedbackId = 1L;
	        when(mockFeedbackDao.deleteById(feedbackId)).thenThrow(new RuntimeException("Database error"));

	        boolean result = feedbackService.deleteFeedbackById(feedbackId);

	        assertFalse(result);
	        verify(mockFeedbackDao).deleteById(feedbackId);
	    }

	    @Test
	    public void testDeleteFeedbackException() throws Exception {
	        Feedback feedback = new Feedback();
	        when(mockFeedbackDao.delete(feedback)).thenThrow(new RuntimeException("Database error"));

	        boolean result = feedbackService.deleteFeedback(feedback);

	        assertFalse(result);
	        verify(mockFeedbackDao).delete(feedback);
	    }

	    @Test
	    public void testFindFeedbackByIdException() throws Exception {
	        Long feedbackId = 1L;
	        when(mockFeedbackDao.findById(feedbackId)).thenThrow(new RuntimeException("Database error"));

	        Feedback result = feedbackService.findFeedbackById(feedbackId);

	        assertNull(result);
	        verify(mockFeedbackDao).findById(feedbackId);
	    }

	    @Test
	    public void testFindAllFeedbackException() throws Exception {
	        when(mockFeedbackDao.findAll()).thenThrow(new RuntimeException("Database error"));

	        List<Feedback> result = feedbackService.findAllFeedback();

	        assertNull(result);
	        verify(mockFeedbackDao).findAll();
	    }

	    @Test
	    public void testFindFeedbackByPersonIdException() throws Exception {
	        Long personId = 1L;
	        when(mockFeedbackDao.findByPersonId(personId)).thenThrow(new RuntimeException("Database error"));

	        List<Feedback> result = feedbackService.findFeedbackByPersonId(personId);

	        assertNull(result);
	        verify(mockFeedbackDao).findByPersonId(personId);
	    }

	    @Test
	    public void testFindFeedbackByProductIdException() throws Exception {
	        Long productId = 1L;
	        when(mockFeedbackDao.findByProductId(productId)).thenThrow(new RuntimeException("Database error"));

	        List<Feedback> result = feedbackService.findFeedbackByProductId(productId);

	        assertNull(result);
	        verify(mockFeedbackDao).findByProductId(productId);
	    }
}
