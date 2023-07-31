package com.example.miniproject2.Dto;

import com.example.miniproject2.Entity.Comment;
import lombok.Data;

@Data
public class CommentDto {
    private Long commentId;
    private String writer;
    private String password;
    private String content;
    private String reply;
    private Long itemId;

    public static CommentDto fromEntity(Comment entity) {
        CommentDto dto = new CommentDto();
        dto.setCommentId(entity.getId());
        dto.setWriter(entity.getWriter());
        dto.setPassword(entity.getPassword());
        dto.setContent(entity.getContent());
        dto.setReply(entity.getReply());
        dto.setItemId(entity.getItemId());

        return dto;
    }

}
