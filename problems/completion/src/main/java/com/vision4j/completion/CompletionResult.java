package com.vision4j.completion;

import java.awt.image.BufferedImage;

public class CompletionResult {

    private BufferedImage bufferedImage;

    public CompletionResult(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }
}
