<?xml version="1.0" encoding="UTF-8"?>
<ns:AckRequest xmlns:ns="urn://x-artefacts-smev-gov-ru/services/message-exchange/types/1.1"
               xmlns:ns1="urn://x-artefacts-smev-gov-ru/services/message-exchange/types/basic/1.1">
    <ns1:AckTargetMessage Id="0" accepted="false">${headers.businessMessageId}</ns1:AckTargetMessage>
    <ns:CallerInformationSystemSignature>
        <ds:Signature xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
            <ds:SignedInfo>
                <ds:CanonicalizationMethod Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/>
                <ds:SignatureMethod Algorithm="http://www.w3.org/2001/04/xmldsig-more#gostr34102001-gostr3411"/>
                <ds:Reference URI="#SIGNED_BY_CALLER">
                    <ds:Transforms>
                        <ds:Transform Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/>
                        <ds:Transform Algorithm="urn://smev-gov-ru/xmldsig/transform"/>
                    </ds:Transforms>
                    <ds:DigestMethod Algorithm="http://www.w3.org/2001/04/xmldsig-more#gostr3411"/>
                    <ds:DigestValue>iYwGGJIG7q3AuiIBGC8G/Uk50FIIJmC+Vxf24dbh15I=</ds:DigestValue>
                </ds:Reference>
            </ds:SignedInfo>
            <ds:SignatureValue>
                EoG4oOy27laTo5/vNL57yBvBXimQkE8NpIXa9XDVAU05tsFgKecdRjsuEOkBVAtSFKmUZZtNs3LNZwmuct5r/g==
            </ds:SignatureValue>
            <ds:KeyInfo>
                <ds:X509Data>
                    <ds:X509Certificate>
                        MIIBeDCCASWgAwIBAgIEMRFBsjAKBgYqhQMCAgMFADAxMRAwDgYDVQQLEwdTWVNURU0xMRAwDgYDVQQKEwdtdGZvaXYxMQswCQYDVQQGEwJSVTAeFw0xNDA5MDQwOTIxNDVaFw0xNDEyMDMwOTIxNDVaMDExEDAOBgNVBAsTB1NZU1RFTTExEDAOBgNVBAoTB210Zm9pdjExCzAJBgNVBAYTAlJVMGMwHAYGKoUDAgITMBIGByqFAwICIwEGByqFAwICHgEDQwAEQEJ2ZSpw1tSo/cylifftd268aXKoMnQBUyx6Oj/t/jKF9Btue0la3cQjNGkTnwMC2G0towX/atXIskbaWXoX6ZSjITAfMB0GA1UdDgQWBBRy0XtQ9B2w/p/OR0jEH8N7jC5FSDAKBgYqhQMCAgMFAANBANCHlS68Rzs2eYiRfTwk1EnQzCTjNFjG4DdDW5/vK8I2hQ/v4vcmV+CCUjt1IZb2PiAVpl1pIU8oAEe3WGxbAwM=
                    </ds:X509Certificate>
                </ds:X509Data>
            </ds:KeyInfo>
        </ds:Signature>
    </ns:CallerInformationSystemSignature>
</ns:AckRequest>