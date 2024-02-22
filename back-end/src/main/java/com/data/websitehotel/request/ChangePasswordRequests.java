package com.data.websitehotel.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class ChangePasswordRequests {
    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}
