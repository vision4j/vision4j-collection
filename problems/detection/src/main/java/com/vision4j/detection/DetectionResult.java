package com.vision4j.detection;

import com.vision4j.utils.Category;

import java.util.HashMap;
import java.util.List;

public class DetectionResult extends HashMap<Category, List<DetectionResult.BoundingBox>> {

    public static class BoundingBox {
        private int left;
        private int top;
        private int right;
        private int bottom;

        public BoundingBox(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
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
