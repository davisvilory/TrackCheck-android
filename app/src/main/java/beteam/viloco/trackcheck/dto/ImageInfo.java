package beteam.viloco.trackcheck.dto;

public class ImageInfo {
    public final String filePath;
    public final String imageOrientation;

    public ImageInfo(String filePath, String imageOrientation) {
        this.filePath = filePath;

        if (imageOrientation == null) imageOrientation = "0";
        this.imageOrientation = imageOrientation;
    }
}