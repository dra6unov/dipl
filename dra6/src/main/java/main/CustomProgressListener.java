package main;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;

@Service
public class CustomProgressListener implements MediaHttpDownloaderProgressListener {
	  public void progressChanged(MediaHttpDownloader downloader) {
	    switch (downloader.getDownloadState()) {
	      case MEDIA_IN_PROGRESS:
	        System.out.println(downloader.getProgress());
	        break;
	      case MEDIA_COMPLETE:
	        System.out.println("Download is complete!");
	    }
	  }
	}
