package com.gestionate.backend.communication.application;

import com.gestionate.backend.communication.interfaces.rest.dto.ContactMessageRequest;
import com.gestionate.backend.communication.interfaces.rest.dto.ContactMessageResponse;

public interface IContactMessageService {

    ContactMessageResponse sendMessage(ContactMessageRequest request);
}
