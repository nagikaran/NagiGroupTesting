package com.NagiGroup.utility;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiResponse<T> {
    /// <summary>
    /// This property will be set to true if the API response is successully and false otherwise
    /// </summary>
    public boolean Success;

	/// <summary>
    /// Error meessage will be set to this property along with the Success boolean flag.
    /// </summary>
    public String Message;

    /// <summary>
    /// Indicates whether API authentication failed or not
    /// </summary>
    public boolean IsAuthFailure;

    /// <summary>
    /// API response data
    /// </summary>
    public T Data;

    /// <summary>
    /// API response data Total Count 
    /// </summary>
    public int TotalRecord;
}
