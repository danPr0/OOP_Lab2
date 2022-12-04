<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/schedules">
        <html lang="en">
            <head>
                <title>Schedule</title>

                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css"/>
                <link rel="stylesheet" href="C:\Users\HP 840G4\IdeaProjects\oop\lab2\src\main\resources\styles.css"/>
            </head>
            <body>
                <div class="ms-4 mt-3">
                    <ul class="list-group list-group-striped striped-list">
                        <xsl:for-each select="schedule">
                            <li class="list-group-item mb-3">
                                <p>Author : <xsl:value-of select="author"/></p>
                                <p>Faculty : <xsl:value-of select="faculty"/></p>
                                <p>Department : <xsl:value-of select="department"/></p>
                                <p>Audiences :
                                    <br/>
                                    <ul>
                                        <xsl:for-each select="audiences/audience">
                                            <li>
                                                <xsl:value-of select="number"/> (<xsl:value-of select="description"/>)
                                            </li>
                                        </xsl:for-each>
                                    </ul>
                                </p>
                                <p>Students : <xsl:value-of select="students"/></p>
                            </li>
                        </xsl:for-each>
                    </ul>
                </div>
                <!--                <table class="table table-light table-striped table-hover">-->
                <!--                    <tr>-->
                <!--                        <th>Author</th>-->
                <!--                        <th>Faculty</th>-->
                <!--                        <th>Department</th>-->
                <!--                        <th>Audience</th>-->
                <!--                        <th>Curriculum</th>-->
                <!--                        <th>Students</th>-->
                <!--                    </tr>-->
                <!--                    <xsl:for-each select="timetable">-->
                <!--                        <tr>-->
                <!--                            <td>-->
                <!--                                <xsl:value-of select="author"/>-->
                <!--                            </td>-->
                <!--                            <td>-->
                <!--                                <xsl:value-of select="faculty"/>-->
                <!--                            </td>-->
                <!--                            <td>-->
                <!--                                <xsl:value-of select="department"/>-->
                <!--                            </td>-->
                <!--                            <td>-->
                <!--                                <ul>-->
                <!--                                    <xsl:for-each select="audience">-->
                <!--                                        <li><xsl:value-of select="text()"/></li>-->
                <!--                                    </xsl:for-each>-->
                <!--                                </ul>-->
                <!--                            </td>-->
                <!--                            <td>-->
                <!--                                <xsl:value-of select="curriculum"/>-->
                <!--                            </td>-->
                <!--                            <td>-->
                <!--                                <xsl:value-of select="students"/>-->
                <!--                            </td>-->
                <!--                        </tr>-->
                <!--                    </xsl:for-each>-->
                <!--                </table>-->
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>