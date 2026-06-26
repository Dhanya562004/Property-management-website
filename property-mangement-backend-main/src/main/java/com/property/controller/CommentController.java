package com.property.controller;

import com.property.dto.ApiResponse;
import com.property.model.Comment;
import com.property.model.Property;
import com.property.model.Reply;
import com.property.repository.CommentRepository;
import com.property.repository.PropertyRepository;
import com.property.repository.ReplyRepository;
import com.property.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @PostMapping("/manage/post")
    public ApiResponse postComment(@RequestBody Map<String, String> body) {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }

            if (currentUser.getRole() != 1) { // 1 = ADMIN (only Admin can post comment as per postComments.js)
                return ApiResponse.noAccess();
            }

            String commentText = body.get("comment");
            String propertyIdStr = body.get("property_id");

            if (commentText == null || commentText.trim().isEmpty()) {
                return ApiResponse.requiredParam("Comment");
            }
            if (propertyIdStr == null || propertyIdStr.trim().isEmpty()) {
                return ApiResponse.requiredParam("property_id");
            }

            Long propertyId = Long.parseLong(propertyIdStr);
            Optional<Property> optProp = propertyRepository.findById(propertyId);
            if (!optProp.isPresent() || optProp.get().getIsActive() != 1) {
                return ApiResponse.invalidInput("Property not found");
            }

            Comment comment = new Comment();
            comment.setComment(commentText);
            comment.setProperty(optProp.get());
            commentRepository.save(comment);

            return ApiResponse.success();

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @GetMapping("/manage/list")
    public ApiResponse listComments(@RequestParam("property_id") Long propertyId) {
        try {
            Optional<Property> optProp = propertyRepository.findById(propertyId);
            if (!optProp.isPresent() || optProp.get().getIsActive() != 1) {
                return ApiResponse.success(new ArrayList<>());
            }

            List<Comment> comments = commentRepository.findByPropertyAndIsActiveOrderByPostedOnDesc(optProp.get(), 1);
            List<Map<String, Object>> responseList = new ArrayList<>();

            for (Comment c : comments) {
                Map<String, Object> cMap = new HashMap<>();
                cMap.put("_id", c.getId().toString());
                cMap.put("id", c.getId());
                cMap.put("comment", c.getComment());
                cMap.put("isactive", c.getIsActive());
                cMap.put("posted_on", c.getPostedOn());

                List<Reply> replies = replyRepository.findByCommentAndIsActiveOrderByPostedOnDesc(c, 1);
                List<Map<String, Object>> replyList = new ArrayList<>();

                for (Reply r : replies) {
                    Map<String, Object> rMap = new HashMap<>();
                    rMap.put("_id", r.getId().toString());
                    rMap.put("id", r.getId());
                    rMap.put("replycmt", r.getReplycmt());
                    rMap.put("isactive", r.getIsActive());
                    rMap.put("posted_on", r.getPostedOn());
                    replyList.add(rMap);
                }

                cMap.put("replies", replyList);
                responseList.add(cMap);
            }

            return ApiResponse.success(responseList);

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @PostMapping("/manage/reply")
    public ApiResponse postReply(@RequestBody Map<String, String> body) {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }

            if (currentUser.getRole() != 2) { // 2 = VENDOR (only Vendor can reply)
                return ApiResponse.noAccess();
            }

            String replyText = body.get("reply");
            String commentIdStr = body.get("comment_id");

            if (replyText == null || replyText.trim().isEmpty()) {
                return ApiResponse.requiredParam("reply");
            }
            if (commentIdStr == null || commentIdStr.trim().isEmpty()) {
                return ApiResponse.requiredParam("comment_id");
            }

            Long commentId = Long.parseLong(commentIdStr);
            Optional<Comment> optCmt = commentRepository.findById(commentId);
            if (!optCmt.isPresent() || optCmt.get().getIsActive() != 1) {
                return ApiResponse.invalidInput("Comment not found");
            }

            Reply reply = new Reply();
            reply.setReplycmt(replyText);
            reply.setComment(optCmt.get());
            replyRepository.save(reply);

            return ApiResponse.success();

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }
}
