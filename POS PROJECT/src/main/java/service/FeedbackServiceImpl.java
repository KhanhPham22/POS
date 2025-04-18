package service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.FeedbackDao;
import model.Feedback;

import java.util.List;

public class FeedbackServiceImpl implements FeedbackService {

    private static final Logger Log = LogManager.getLogger(FeedbackServiceImpl.class);
    private final FeedbackDao feedbackDao;

    public FeedbackServiceImpl(FeedbackDao feedbackDao) {
        this.feedbackDao =  feedbackDao;
        this.feedbackDao.setClass(Feedback.class);
    }

    @Override
    public boolean createFeedback(Feedback feedback) {
        try {
            return feedbackDao.create(feedback);
        } catch (Exception e) {
            Log.error("Failed to create feedback", e);
            return false;
        }
    }

    @Override
    public boolean updateFeedback(Feedback feedback) {
        try {
            return feedbackDao.update(feedback);
        } catch (Exception e) {
            Log.error("Failed to update feedback", e);
            return false;
        }
    }

    @Override
    public boolean deleteFeedbackById(Long feedbackId) {
        try {
            return feedbackDao.deleteById(feedbackId);
        } catch (Exception e) {
            Log.error("Failed to delete feedback with ID: " + feedbackId, e);
            return false;
        }
    }

    @Override
    public boolean deleteFeedback(Feedback feedback) {
        try {
            return feedbackDao.delete(feedback);
        } catch (Exception e) {
            Log.error("Failed to delete feedback", e);
            return false;
        }
    }

    @Override
    public Feedback findFeedbackById(Long feedbackId) {
        try {
            return feedbackDao.findById(feedbackId);
        } catch (Exception e) {
            Log.error("Failed to retrieve feedback with ID: " + feedbackId, e);
            return null;
        }
    }

    @Override
    public List<Feedback> findAllFeedback() {
        try {
            return feedbackDao.findAll();
        } catch (Exception e) {
            Log.error("Failed to retrieve all feedback", e);
            return null;
        }
    }
    
    @Override
    public List<Feedback> findFeedbackByPersonId(Long personId) {
        try {
            return feedbackDao.findByPersonId(personId);
        } catch (Exception e) {
            Log.error("Failed to retrieve feedback for user with ID: " + personId, e);
            return null;
        }
    }

    @Override
    public List<Feedback> findFeedbackByProductId(Long productId) {
        try {
            return feedbackDao.findByProductId(productId);
        } catch (Exception e) {
            Log.error("Failed to retrieve feedback for product with ID: " + productId, e);
            return null;
        }
    }
}



