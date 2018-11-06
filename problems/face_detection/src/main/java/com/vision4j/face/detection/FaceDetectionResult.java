package com.vision4j.face.detection;

import java.util.ArrayList;

public class FaceDetectionResult extends ArrayList<FaceDetectionResult.BoundingBox> {

    public static class BoundingBox {
        /**
         * Name of the person
         */
        private String identifier;
        private int left;
        private int top;
        private int right;
        private int bottom;

        public BoundingBox(int left, int top, int right, int bottom) {
            this("auto" + left + top + right + bottom, left, top, right, bottom);
        }

        public BoundingBox(String identifier, int left, int top, int right, int bottom) {
            this.identifier = identifier;
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public String getIdentifier() {
            return identifier;
        }

        public int getLeft() {
            return left;
        }

        public int getTop() {
            return top;
        }

        public int getRight() {
            return right;
        }

        public int getBottom() {
            return bottom;
        }
    }

}
