package com.strategicgains.restexpress.plugin.swagger.domain;

/**
 * ApiResponse object to match Swagger annotation @ApiResponse
 * 
 * @author uritzry Since May 1, 2014
 * @since May 1, 2014
 */
public class ApiResponse
extends DataType
{
	// HTTP Response Code
	private int code;

	// Reason for the response code used.
	private String reason;

	public ApiResponse(int code, String message)
	{
		this.code = code;
		this.reason = message;
	}

	public int getCode()
	{
		return code;
	}

	public String getReason()
	{
		return reason;
	}
}
