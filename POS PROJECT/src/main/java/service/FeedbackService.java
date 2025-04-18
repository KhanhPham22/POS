package service;

import java.util.List;

import model.Feedback;

public interface FeedbackService {
    boolean createFeedback(Feedback feedback);
    
    boolean updateFeedback(Feedback feedback);
    
    boolean deleteFeedbackById(Long feedbackId);
    
    boolean deleteFeedback(Feedback feedback);
    
    Feedback findFeedbackById(Long feedbackId);
    
    List<Feedback> findAllFeedback();

	List<Feedback> findFeedbackByProductId(Long productId);

	List<Feedback> findFeedbackByPersonId(Long personId);
}