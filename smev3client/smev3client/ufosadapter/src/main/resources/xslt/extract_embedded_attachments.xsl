<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
    <Attachments>
        <xsl:apply-templates select="//*:AttachmentHeaderList/*:AttachmentHeader"/>
    </Attachments>
</xsl:template>

<xsl:template match="//*:AttachmentHeaderList/*:AttachmentHeader">
    <xsl:variable name="contentId" select="*:contentId/text()"></xsl:variable>

    <Attachment>
        <contentId><xsl:value-of select="$contentId"/></contentId>
        <mimeType><xsl:value-of select="*:MimeType/text()"/></mimeType>
        <signature><xsl:value-of select="*:SignaturePKCS7/text()"/></signature>
        <content><xsl:value-of select="//*:AttachmentContentList/*:AttachmentContent[*:Id = $contentId]/*:Content/text()"/></content>
    </Attachment>
</xsl:template>

</xsl:stylesheet>