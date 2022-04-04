package com.zugazagoitia.knag.users.model.responses;

import lombok.*;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class ApiResponse {

	private final int status;

	private final String response;

}
