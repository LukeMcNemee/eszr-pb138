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
                <unit>g</unit>
                <amount><xsl:value-of select="amount * 5"/></amount>
            </xsl:if>
            <xsl:if test="unit/text() = 'g' ">
                <unit>g</unit>
                <amount><xsl:value-of select="amount"/></amount>                
            </xsl:if>
            <xsl:if test="unit/text() = 'kg' ">
                <unit>g</unit>
                <amount><xsl:value-of select="amount * 1000"/></amount>
            </xsl:if>
            <xsl:if test="unit/text() = 'špetka' ">
                <unit>g</unit>
                <amount><xsl:value-of select="amount"/></amount>
            </xsl:if>
            <xsl:if test="unit/text() = 'l' ">
                <unit>ml</unit>
                <amount><xsl:value-of select="amount * 1000"/></amount>
            </xsl:if>
            <xsl:if test="unit/text() = 'ml' ">
                <unit>ml</unit>
                <amount><xsl:value-of select="amount"/></amount>
            </xsl:if>
            <xsl:if test="unit/text() = 'dkg' ">
                <unit>g</unit>
                <amount><xsl:value-of select="amount * 10"/></amount>
            </xsl:if>
            <xsl:if test="unit/text() = 'lžíce' ">
                <unit>g</unit>
                <amount><xsl:value-of select="amount * 15"/></amount>
            </xsl:if>
            <xsl:if test="unit/text() = 'hrnek' ">
                <unit>ml</unit>
                <amount><xsl:value-of select="amount * 230"/></amount>
            </xsl:if>
            <xsl:if test="unit/text() = 'plechovka' ">
                <unit>g</unit>
                <amount><xsl:value-of select="amount * 400"/></amount>
            </xsl:if>
            <xsl:if test="unit/text() = 'cl' ">
                <unit>ml</unit>
                <amount><xsl:value-of select="amount * 10"/></amount>
            </xsl:if>
        </part>        
    </xsl:template>
</xsl:stylesheet>