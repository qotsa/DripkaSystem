<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="breadcrumbId"/>

    <xsl:output omit-xml-declaration="yes"/>

    <xsl:template match="node()|@*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="*:AttachmentContent">
        <xsl:variable name="contentId" select="*:Id/text()"></xsl:variable>
        <AttachmentContent xmlns="urn://x-artefacts-smev-gov-ru/services/message-exchange/types/basic/1.1">
            <Id><xsl:value-of select="$contentId"/></Id>
            <Content>FTP:<xsl:value-of select="$breadcrumbId"/>/<xsl:value-of select="$contentId"/>.raw</Content>
        </AttachmentContent>
    </xsl:template>

</xsl:stylesheet>