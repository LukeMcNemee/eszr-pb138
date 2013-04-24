<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0">
    <xsl:output method="xml"
        doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
        doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
        encoding="UTF-8"
        indent="yes"
    />
    <xsl:template match="receipts">
        <receipts xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:noNamespaceSchemaLocation="Receipts_converted_schema.xsd" xmlns="">
            <xsl:apply-templates select="receipt"/>
        </receipts>
    </xsl:template>
    
    <xsl:template match="receipt">
        <receipt>
            <name><xsl:value-of select="name/text()"/></name>
            <parts>
                <xsl:apply-templates select="parts/part"/>                
            </parts>
        </receipt>        
    </xsl:template>
    <xsl:template match="part">
        <part>
            <ingredient><xsl:value-of select="ingredient/text()"/></ingredient>
            <xsl:if test="unit/text() = 'ks' ">
                <unit>ks</unit>
                <amount><xsl:value-of select="amount"/></amount>
            </xsl:if>
            <xsl:if test="unit/text() = 'lžička' ">
                <unit>kg</unit>
                <amount><xsl:value-of select="amount div 202"/></amount>
            </xsl:if>
            <xsl:if test="unit/text() = 'g' ">
                <unit>kg</unit>
                <amount><xsl:value-of select="amount div 1000"/></amount>                
            </xsl:if>
            <xsl:if test="unit/text() = 'kg' ">
                <unit>kg</unit>
                <amount><xsl:value-of select="amount"/></amount>
            </xsl:if>
            <xsl:if test="unit/text() = 'špetka' ">
                <unit>kg</unit>
                <amount><xsl:value-of select="amount div 2000"/></amount>
            </xsl:if>
            <xsl:if test="unit/text() = 'l' ">
                <unit>l</unit>
                <amount><xsl:value-of select="amount"/></amount>
            </xsl:if>
            <xsl:if test="unit/text() = 'ml' ">
                <unit>l</unit>
                <amount><xsl:value-of select="amount div 1000"/></amount>
            </xsl:if>
            <xsl:if test="unit/text() = 'dkg' ">
                <unit>kg</unit>
                <amount><xsl:value-of select="amount div 100"/></amount>
            </xsl:if>
            <xsl:if test="unit/text() = 'lžíce' ">
                <unit>kg</unit>
                <amount><xsl:value-of select="amount div 100"/></amount>
            </xsl:if>
            <xsl:if test="unit/text() = 'hrnek' ">
                <unit>l</unit>
                <amount><xsl:value-of select="amount div 8"/></amount>
            </xsl:if>
            <xsl:if test="unit/text() = 'plechovka' ">
                <unit>l</unit>
                <amount><xsl:value-of select="amount div 2.5"/></amount>
            </xsl:if>
            <xsl:if test="unit/text() = 'cl' ">
                <unit>l</unit>
                <amount><xsl:value-of select="amount div 100"/></amount>
            </xsl:if>
        </part>        
    </xsl:template>
</xsl:stylesheet>