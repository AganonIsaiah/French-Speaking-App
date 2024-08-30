package com.example.frenchlearningapp.service;

import java.io.File;

import org.springframework.stereotype.Service;

import com.assemblyai.api.AssemblyAI;
import com.assemblyai.api.resources.transcripts.requests.TranscriptParams;
import com.assemblyai.api.resources.transcripts.types.SpeechModel;
import com.assemblyai.api.resources.transcripts.types.TranscriptLanguageCode;

/**
 * Utilizes AssemblyAI for transcription
 */
@Service
public class TranscriptionService {

 
    private static String API = "41e2ffa0850f445d908cb2e68d572b3c";
    /**
     * Transcribes text
     * 
     * @param The path to the user's recording
     * @return The recorded.mp3 transcription
     */
    public String transcribe(String filePath) throws Exception {
        AssemblyAI client = AssemblyAI.builder()
        .apiKey(API)
        .build();

        var uploadedFile = client.files().upload(new File(filePath));
        var fileUrl = uploadedFile.getUploadUrl();

        
        var transcript = client.transcripts().transcribe(TranscriptParams.builder()
            .audioUrl(fileUrl)
            .speechModel(SpeechModel.BEST)
            .languageCode(TranscriptLanguageCode.FR)
            .build()
        );

        /* Removing unnecessary brackets from transcript */
        StringBuilder sb = new StringBuilder(transcript.getText().toString());
        sb.delete(0,9);
        sb.deleteCharAt(sb.length()-1);

        return sb.toString();
    }

}