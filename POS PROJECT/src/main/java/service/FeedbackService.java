package service;

import java.util.List;

import model.Feedback;

public interface FeedbackService {
    boolean createFeedback(Feedback feedback);
    
    boolean updateFeedback(Feedback feedback);
    
    boolean deleteFeedbackById(long feedbackId);
    
    boolean deleteFeedback(Feedback feedback);
    
    Feedback findFeedbackById(long feedbackId);
    
    List<Feedback> findAllFeedback(int pageNumber, int pageSize);

	List<Feedback> findFeedbackByProductId(long productId);

	List<Feedback> findFeedbackByPersonId(long personId);
}