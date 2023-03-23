package com.sinam7.KookminNoticeBot.OCR;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

import static com.sinam7.KookminNoticeBot.Constants.OCR_DATAPATH;

@Slf4j
public class OCR {

    public String run(File file) {
        ITesseract instance = new Tesseract();
        instance.setDatapath(OCR_DATAPATH);
        instance.setLanguage("kor");

        String result;
        try {
            log.info("OCR started");
            result = instance.doOCR(file);
//            log.info("OCR result = {}", result);
        } catch (TesseractException e) {
            log.error("Exception occurred whilst running OCR to file {}: {}", file.getPath(), e.getMessage());
            throw new RuntimeException(e);
        }

        return result;
    }
}
