package ru.otr.integration.smev3client.endpoint;

import ru.it.smev.message_exchange.autogenerated.service.v1_2.*;
import ru.it.smev.message_exchange.autogenerated.types.basic.v1_2.Void;
import ru.it.smev.message_exchange.autogenerated.types.v1_2.*;

/**
 * Created by tartanov.mikhail on 09.08.2016.
 */
public class SMEVMessageExchangeServiceEndpoint implements SMEVMessageExchangePortType{
    @Override
    public SendRequestResponse sendRequest(SendRequestRequest parameters) throws AccessDeniedException, AttachmentContentMiscoordinationException, AttachmentSizeLimitExceededException, BusinessDataTypeIsNotSupportedException, DestinationOverflowException, EndOfLifeException, InvalidContentException, InvalidMessageIdFormatException, MessageIsAlreadySentException, QuoteLimitExceededException, RecipientIsNotFoundException, SMEVFailureException, SenderIsNotRegisteredException, SignatureVerificationFaultException, StaleMessageIdException, TransactionCodeInvalidException {
        return null;
    }

    @Override
    public SendResponseResponse sendResponse(SendResponseRequest parameters) throws AttachmentContentMiscoordinationException, AttachmentSizeLimitExceededException, BusinessDataTypeIsNotSupportedException, DestinationOverflowException, IncorrectResponseContentTypeException, InvalidContentException, InvalidMessageIdFormatException, MessageIsAlreadySentException, QuoteLimitExceededException, RecipientIsNotFoundException, SMEVFailureException, SenderIsNotRegisteredException, SignatureVerificationFaultException, StaleMessageIdException {
        return null;
    }

    @Override
    public GetRequestResponse getRequest(GetRequestRequest parameters) throws InvalidContentException, SMEVFailureException, SenderIsNotRegisteredException, SignatureVerificationFaultException, UnknownMessageTypeException {
        return null;
    }

    @Override
    public GetStatusResponse getStatus(GetStatusRequest parameters) throws InvalidContentException, SMEVFailureException, SenderIsNotRegisteredException, SignatureVerificationFaultException, UnknownMessageTypeException {
        return null;
    }

    @Override
    public GetResponseResponse getResponse(GetResponseRequest parameters) throws InvalidContentException, SMEVFailureException, SenderIsNotRegisteredException, SignatureVerificationFaultException, UnknownMessageTypeException {
        return null;
    }

    @Override
    public Void ack(AckRequest parameters) throws InvalidContentException, SMEVFailureException, SenderIsNotRegisteredException, SignatureVerificationFaultException, TargetMessageIsNotFoundException {
        return null;
    }

    @Override
    public GetIncomingQueueStatisticsResponse getIncomingQueueStatistics(GetIncomingQueueStatisticsRequest parameters) throws InvalidContentException, SMEVFailureException, SenderIsNotRegisteredException, SignatureVerificationFaultException {
        GetIncomingQueueStatisticsResponse response = new GetIncomingQueueStatisticsResponse();
        return response;
    }
}