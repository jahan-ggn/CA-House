package com.example.tjg.cahouseclient.JavaFiles;

import android.widget.ImageView;

import com.example.tjg.cahouseclient.R;

public class DownloadImages {
    public void setFileLogo(String filename, ImageView img_filetype) {

        if (filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("doc") ||
                filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("docx")) {
            // Word document
            img_filetype.setImageResource(R.drawable.doc);
        } else if (filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("pdf")) {
            // PDF file
            img_filetype.setImageResource(R.drawable.pdf);
        } else if (filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("ppt") ||
                filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("pptx")) {
            // Powerpoint file
            img_filetype.setImageResource(R.drawable.ppt);
        } else if (filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("xls") ||
                filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("xlsx")) {
            // Excel file
            img_filetype.setImageResource(R.drawable.excel);
        } else if (filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("zip") ||
                filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("rar")) {
            // ZIP file
            img_filetype.setImageResource(R.drawable.zip);
        } else if (filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("rtf")) {
            // RTF file
            img_filetype.setImageResource(R.drawable.rtf);
        } else if (filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("wav")) {
            // WAV audio file
            img_filetype.setImageResource(R.drawable.wav);
        } else if (filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("mp3")) {
            //MP3 file
            img_filetype.setImageResource(R.drawable.mp3);
        } else if (filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("gif")) {
            // GIF file
            img_filetype.setImageResource(R.drawable.gif);
        } else if (filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("jpg") ||
                filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("jpeg") ||
                filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("png")) {
            // JPG file
            img_filetype.setImageResource(R.drawable.jpeg);

        } else if (filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("txt")) {
            // Text file
            img_filetype.setImageResource(R.drawable.text);
        } else if (filename.substring(filename.toString().lastIndexOf(".") + 1).equalsIgnoreCase("3gp") ||
                filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("mpg") ||
                filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("mpeg") ||
                filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("mpe") ||
                filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("mp4") ||
                filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase("avi")) {
            // Video files
            img_filetype.setImageResource(R.drawable.mp4);
        }
    }
}
