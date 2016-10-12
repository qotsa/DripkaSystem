<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <Attachments>
            <xsl:apply-templates select="//*:FSAttachmentsList/*:FSAttachment"/>
        </Attachments>
    </xsl:template>

    <xsl:template match="//*:FSAttachmentsList/*:FSAttachment">
        <xsl:variable name="uuid" select="*:uuid/text()"></xsl:variable>

        <Attachment>
            <uuid><xsl:value-of select="$uuid"/></uuid>
            <username><xsl:value-of select="*:UserName/text()"/></username>
            <password><xsl:value-of select="*:Password/text()"/></password>
            <filename><xsl:value-of select="*:FileName/text()"/></filename>
            <hash><xsl:value-of select="//*:RefAttachmentHeaderList/*:RefAttachmentHeader[*:uuid = $uuid]/*:Hash/text()"/></hash>
            <mimeType><xsl:value-of select="//*:RefAttachmentHeaderList/*:RefAttachmentHeader[*:uuid = $uuid]/*:MimeType/text()"/></mimeType>
        </Attachment>
    </xsl:template>

</xsl:stylesheet>