package com.ath.adminefectivo.dto;


import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DownloadDTO  {

	private Long id;
	private String name;
	private String url;
	private InputStream file;
}
