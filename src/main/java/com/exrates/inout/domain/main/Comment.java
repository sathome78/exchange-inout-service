package com.exrates.inout.domain.main;

import com.exrates.inout.domain.enums.UserCommentTopicEnum;
import com.exrates.inout.domain.serializer.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime creationTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime editTime;
    private User creator;
    private String comment;
    private boolean messageSent;
    private int id;
    private User user;
    private UserCommentTopicEnum userCommentTopic;
    private boolean isEditable;
}
