package com.hit.userservice.util;

import com.hit.userservice.exception.BadRequestException;
import com.hit.userservice.util.constant.AppConstants;

public class AppUtils {
    public static void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Số trang không thể nhỏ hơn 0.");
        }
        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Kích thước trang không được vượt quá " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
