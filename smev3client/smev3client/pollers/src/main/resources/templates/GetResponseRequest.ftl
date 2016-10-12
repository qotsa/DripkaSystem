<?xml version="1.0" encoding="utf-8"?>
<typ:GetResponseRequest
        xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
        xmlns:typ="http://otr.ru/irs/services/message-exchange/types"
        xmlns:bas="http://otr.ru/irs/services/message-exchange/types/basic"
        xmlns:ns="urn://x-artefacts-smev-gov-ru/services/message-exchange/types/1.1"
        xmlns:ns1="urn://x-artefacts-smev-gov-ru/services/message-exchange/types/basic/1.1">
    <bas:MessageTypeSelector Id="SIGNED_BY_CALLER"
                             xmlns:ns2="urn://x-artefacts-smev-gov-ru/services/message-exchange/types/basic/1.1"
                             xmlns="urn://x-artefacts-smev-gov-ru/services/message-exchange/types/1.1">
        <bas:NamespaceURI>NamespaceURI</bas:NamespaceURI>
        <bas:RootElementLocalName>RootElementLocalName</bas:RootElementLocalName>
        <bas:Timestamp>2014-02-11T17:10:03.616+04:00</bas:Timestamp>
        <!--Optional:-->
        <bas:NodeID>q1</bas:NodeID>
    </bas:MessageTypeSelector>
    <!--Optional:-->
    <typ:CallerInformationSystemSignature>
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
                7C4yUXubfFseK5eaFQfWsS5eM3+t85lcWqjD3FPGSBcNvYq78t5WMRE/5/5BiLvLww6vq0xM+4sbOH00RTDjYQ==
            </ds:SignatureValue>
            <ds:KeyInfo>
                <ds:X509Data>
                    <ds:X509Certificate>
                        MIIBhzCCATagAwIBAgIFAMFdkFQwCAYGKoUDAgIDMC0xEDAOBgNVBAsTB1NZU1RFTTExDDAKBgNVBAoTA09WMjELMAkGA1UEBhMCUlUwHhcNMTQwMjIxMTMzNDMyWhcNMTUwMjIxMTMzNDMyWjAtMRAwDgYDVQQLEwdTWVNURU0xMQwwCgYDVQQKEwNPVjIxCzAJBgNVBAYTAlJVMGMwHAYGKoUDAgITMBIGByqFAwICJAAGByqFAwICHgEDQwAEQLjcuMDezt3MrljIr+54Cy64Gvgy8uuGgTpjvlrDAkiGdTL/m9EDDJvMARnMjzSb1JTxovUWfTV8j2bns+KZXNyjOzA5MA4GA1UdDwEB/wQEAwID6DATBgNVHSUEDDAKBggrBgEFBQcDAjASBgNVHRMBAf8ECDAGAQH/AgEFMAgGBiqFAwICAwNBAMVRmhKGKFtRbBlGLl++KtOAvm96C5wnj+6L/wMYpw7Gd7WBM21Zqh9wu+3eZotglDsJMEYbKgiLRprSxKz+DHs=
                    </ds:X509Certificate>
                </ds:X509Data>
            </ds:KeyInfo>
        </ds:Signature>
    </typ:CallerInformationSystemSignature>
</typ:GetResponseRequest>
