<?xml version="1.0" encoding="UTF-8"?>
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/" xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Header/>
   <S:Body>
      <GetRequestResponse xmlns="urn://x-artefacts-smev-gov-ru/services/message-exchange/types/1.1" xmlns:ns2="urn://x-artefacts-smev-gov-ru/services/message-exchange/types/basic/1.1" xmlns:ns3="urn://x-artefacts-smev-gov-ru/services/message-exchange/types/faults/1.1">
         <RequestMessage>
            <Request Id="ID_99d4fef2-4d5c-49d5-94b3-37728cb5e11e">
               <SenderProvidedRequestData Id="ID_SREQD">
                  <MessageID>${MessageId}</MessageID>
                  <ReferenceMessageID>${MessageID}</ReferenceMessageID>
                  <TransactionCode>c51d382a-d5c9-477b-9fd5-1e7d09467e79</TransactionCode>
                  <ns2:MessagePrimaryContent>
                     <ns2:ZadorgRequest ИдЗапрос="c51d382a-d5c9-477b-9fd5-1e7d09467e79" xmlns:ns="urn://x-artefacts-smev-gov-ru/services/message-exchange/types/1.1" xmlns:ns1="urn://x-artefacts-smev-gov-ru/services/message-exchange/types/basic/1.1" xmlns:ns2="urn://x-artefacts-fns-zadorg/root/548-04/4.0.4" xmlns:ns4="urn://x-artefacts-smev-gov-ru/services/message-exchange/types/1.1" xmlns:ns5="http://www.otr.com/sufd/smev30SupportService/xml/faultMessage" xmlns:ns6="http://www.otr.com/sufd/smev30SupportService" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
                        <ns2:СвЮЛ ИННЮЛ="3123116900" НаимЮЛ="Управление Федеральной службы по ветеринарному и фитосанитарному надзору по Белгородской области" ОГРН="3123116900000"/>
                        <ns2:ЗапросНП ДатаЗапрСв="2016-08-18">
                           <ns2:ИННЮЛ>1111111111</ns2:ИННЮЛ>
                        </ns2:ЗапросНП>
                     </ns2:ZadorgRequest>
                  </ns2:MessagePrimaryContent>
               </SenderProvidedRequestData>
               <MessageMetadata Id="ID_48020676-cfb8-4567-bb67-f1572846433e">
                  <MessageId>${MessageId}</MessageId>
                  <MessageType>REQUEST</MessageType>
                  <Sender>
                     <Mnemonic>STUB</Mnemonic>
                     <HumanReadableName>Тестовое ведомство</HumanReadableName>
                  </Sender>
                  <SendingTimestamp>2016-08-18T15:19:49.887+03:00</SendingTimestamp>
                  <DestinationName>TODO DestinationName</DestinationName>
                  <Recipient>
                     <Mnemonic>STUB</Mnemonic>
                     <HumanReadableName>Тестовое ведомство</HumanReadableName>
                  </Recipient>
                  <SupplementaryData>
                     <DetectedContentTypeName>TODO SupplementaryData.DetectedContentTypeName</DetectedContentTypeName>
                     <InteractionType>NotDetected</InteractionType>
                  </SupplementaryData>
                  <Status>requestIsAcceptedBySmev</Status>
               </MessageMetadata>
               <ReplyTo>STUB|0d9ba260-653e-11e6-a5b0-005056b573ad|2016-08-18T15:19:49.887+03:00</ReplyTo>
               <SenderInformationSystemSignature>
                  <ds:Signature xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
                     <ds:SignedInfo>
                        <ds:CanonicalizationMethod Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/>
                        <ds:SignatureMethod Algorithm="http://www.w3.org/2001/04/xmldsig-more#gostr34102001-gostr3411"/>
                        <ds:Reference URI="#ID_SREQD">
                           <ds:Transforms>
                              <ds:Transform Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/>
                              <ds:Transform Algorithm="urn://smev-gov-ru/xmldsig/transform"/>
                           </ds:Transforms>
                           <ds:DigestMethod Algorithm="http://www.w3.org/2001/04/xmldsig-more#gostr3411"/>
                           <ds:DigestValue>23e1oauzmA8mhNleChYIinUmaYKC3TxCFRer890vBwQ=</ds:DigestValue>
                        </ds:Reference>
                     </ds:SignedInfo>
                     <ds:SignatureValue>tua9TWmlr37fBYrrD3aEVU391YOhEEX38Np3VFQ6Ew3JcKyEYqbEJJ5g24O1WS/fSMVaNU9vLKMNs0t5XKkJ+Q==</ds:SignatureValue>
                     <ds:KeyInfo>
                        <ds:X509Data>
                           <ds:X509Certificate>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</ds:X509Certificate>
                        </ds:X509Data>
                     </ds:KeyInfo>
                  </ds:Signature>
               </SenderInformationSystemSignature>
            </Request>
            <SMEVSignature>
               <ds:Signature xmlns:ds="http://www.w3.org/2000/09/xmldsig#" xmlns:ns4="urn://x-artefacts-smev-gov-ru/services/message-exchange/types/1.1" xmlns:ns5="http://www.otr.com/sufd/smev30SupportService/xml/faultMessage" xmlns:ns6="http://www.otr.com/sufd/smev30SupportService">
                  <ds:SignedInfo>
                     <ds:CanonicalizationMethod Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/>
                     <ds:SignatureMethod Algorithm="http://www.w3.org/2001/04/xmldsig-more#gostr34102001-gostr3411"/>
                     <ds:Reference URI="#ID_99d4fef2-4d5c-49d5-94b3-37728cb5e11e">
                        <ds:Transforms>
                           <ds:Transform Algorithm="http://www.w3.org/2000/09/xmldsig#enveloped-signature"/>
                           <ds:Transform Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/>
                           <ds:Transform Algorithm="urn://smev-gov-ru/xmldsig/transform"/>
                        </ds:Transforms>
                        <ds:DigestMethod Algorithm="http://www.w3.org/2001/04/xmldsig-more#gostr3411"/>
                        <ds:DigestValue>3I5tmNTvDt5WOleWN2npdFaOohlgO4mmjdsdQxll12Q=</ds:DigestValue>
                     </ds:Reference>
                  </ds:SignedInfo>
                  <ds:SignatureValue>mjXJwrIR7dQsAD0P0h0LGgP8Nr5M39WyFbYrXbx5ZgCnaTLh1D7HOIG2HeTx7qGg33feKdHcul0MLpLLIYhV7A==</ds:SignatureValue>
                  <ds:KeyInfo>
                     <ds:X509Data>
                        <ds:X509Certificate>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</ds:X509Certificate>
                        <ds:X509SubjectName>.1.2.643.100.1=\#120D31303437373936323936343337,STREET=ПЕРЕУЛОК ОРЛИКОВ\, 1/11,CN=РОССЕЛЬХОЗНАДЗОР,O=РОССЕЛЬХОЗНАДЗОР,L=Москва,ST=77 г. Москва,C=RU,EMAILADDRESS=kazekovs@mail.ru,.1.2.643.3.131.1.1=\#120C303037373038353233353330</ds:X509SubjectName>
                     </ds:X509Data>
                  </ds:KeyInfo>
               </ds:Signature>
            </SMEVSignature>
         </RequestMessage>
      </GetRequestResponse>
   </S:Body>
</S:Envelope>