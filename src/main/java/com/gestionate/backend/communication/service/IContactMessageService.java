package com.gestionate.backend.communication.service;

import com.gestionate.backend.communication.dto.ContactMessageRequest;
import com.gestionate.backend.communication.dto.ContactMessageResponse;

public interface IContactMessageService {

    ContactMessageResponse sendMessage(ContactMessageRequest request);
}
