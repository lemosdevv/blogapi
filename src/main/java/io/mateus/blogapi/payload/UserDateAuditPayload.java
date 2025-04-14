package io.mateus.blogapi.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDateAuditPayload extends DateAuditPayload{

    private Long createdBy;

    private Long updatedBy;

}
