package com.gmlive.pngcompressor;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;

class PngUtils {

	private PngUtils() { }

	static File getTempFile(@NonNull Context context, String name, String suffix) {
		try {
			File outputDir = context.getExternalCacheDir();
			return File.createTempFile(name, suffix, outputDir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
