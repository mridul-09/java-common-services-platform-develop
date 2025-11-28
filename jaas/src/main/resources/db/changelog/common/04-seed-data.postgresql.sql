-- liquibase formatted sql

--changeset sathyam:04-seed-data-1
INSERT INTO email_templates (id, name, content)
VALUES (1, 'REGISTER', '
<!doctype html>
<html xmlns=http://www.w3.org/1999/xhtml xmlns:th=http://www.w3.org/1999/xhtml>
<head>
    <!--[if gte mso 9]>
    <xml>
        <o:OfficeDocumentSettings>
            <o:AllowPNG/>
            <o:PixelsPerInch>96</o:PixelsPerInch>
        </o:OfficeDocumentSettings>
    </xml><![endif]-->
    <meta content="text/html; charset=utf-8" http-equiv=Content-Type>
    <meta content="width=device-width" name=viewport>
    <!--[if !mso]><!-->
    <meta content="IE=edge" http-equiv=X-UA-Compatible>
    <!--<![endif]-->
    <title></title>
    <!--[if !mso]><!-->
    <link href="https://fonts.googleapis.com/css?family=Open+Sans" rel=stylesheet>
    <link href="https://fonts.googleapis.com/css?family=Lato" rel=stylesheet>
    <link href="https://fonts.googleapis.com/css?family=Nunito" rel=stylesheet>
    <!--<![endif]-->
    <style>body{margin:0;padding:0}table,td,tr{vertical-align:top;border-collapse:collapse}*{line-height:inherit}a[x-apple-data-detectors=true]{color:inherit!important;text-decoration:none!important}</style>
    <style id=media-query>@media (max-width:500px){.block-grid,.col{min-width:320px!important;max-width:100%!important;display:block!important}.block-grid{width:100%!important}.col{width:100%!important}.col>div{margin:0 auto}img.fullwidth,img.fullwidthOnMobile{max-width:100%!important}.no-stack .col{min-width:0!important;display:table-cell!important}.no-stack.two-up .col{width:50%!important}.no-stack .col.num4{width:33%!important}.no-stack .col.num8{width:66%!important}.no-stack .col.num4{width:33%!important}.no-stack .col.num3{width:25%!important}.no-stack .col.num6{width:50%!important}.no-stack .col.num9{width:75%!important}.video-block{max-width:none!important}.mobile_hide{min-height:0;max-height:0;max-width:0;display:none;overflow:hidden;font-size:0}.desktop_hide{display:block!important;max-height:none!important}}</style>
</head>
<body class=clean-body style=margin:0;padding:0;-webkit-text-size-adjust:100%;background-color:#fff>
<!--[if IE]>
<div class="ie-browser"><![endif]-->
<table bgcolor=#FFFFFF cellpadding=0 cellspacing=0 class=nl-container role=presentation style="table-layout:fixed;vertical-align:top;min-width:320px;Margin:0 auto;border-spacing:0;border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;background-color:#fff;width:100%" valign=top width=100%>
    <tbody>
    <tr style=vertical-align:top valign=top>
        <td style=word-break:break-word;vertical-align:top valign=top>
            <!--[if (mso)|(IE)]>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td align="center" style="background-color:#FFFFFF"><![endif]-->
            <div style=background-color:#f4f4f4>
                <div class=block-grid style="Margin:0 auto;min-width:320px;max-width:480px;overflow-wrap:break-word;word-wrap:break-word;word-break:break-word;background-color:transparent">
                    <div style=border-collapse:collapse;display:table;width:100%;background-color:transparent>
                        <!--[if (mso)|(IE)]>
                        <table width="100%" cellpadding="0" cellspacing="0" border="0"
                               style="background-color:#f4f4f4;">
                            <tr>
                                <td align="center">
                                    <table cellpadding="0" cellspacing="0" border="0" style="width:480px">
                                        <tr class="layout-full-width" style="background-color:transparent"><![endif]-->
                        <!--[if (mso)|(IE)]>
                        <td align="center" width="480"
                            style="background-color:transparent;width:480px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;"
                            valign="top">
                            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td style="padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:5px;">
                        <![endif]-->
                        <div class="col num12" style=min-width:320px;max-width:480px;display:table-cell;vertical-align:top;width:480px>
                            <div style=width:100%!important>
                                <!--[if (!mso)&(!IE)]><!-->
                                <div style="border-top:0 solid transparent;border-left:0 solid transparent;border-bottom:0 solid transparent;border-right:0 solid transparent;padding-top:5px;padding-bottom:5px;padding-right:0;padding-left:0">
                                    <!--<![endif]-->
                                    <div></div>
                                    <!--[if (!mso)&(!IE)]><!-->
                                </div>
                                <!--<![endif]-->
                            </div>
                        </div>
                        <!--[if (mso)|(IE)]></td></tr></table><![endif]-->
                        <!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->
                    </div>
                </div>
            </div>
            <div style=background-color:#f4f4f4>
                <div class=block-grid style="Margin:0 auto;min-width:320px;max-width:480px;overflow-wrap:break-word;word-wrap:break-word;word-break:break-word;background-color:#fff">
                    <div style=border-collapse:collapse;display:table;width:100%;background-color:#fff>
                        <!--[if (mso)|(IE)]>
                        <table width="100%" cellpadding="0" cellspacing="0" border="0"
                               style="background-color:#f4f4f4;">
                            <tr>
                                <td align="center">
                                    <table cellpadding="0" cellspacing="0" border="0" style="width:480px">
                                        <tr class="layout-full-width" style="background-color:#ffffff"><![endif]-->
                        <!--[if (mso)|(IE)]>
                        <td align="center" width="480"
                            style="background-color:#ffffff;width:480px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;"
                            valign="top">
                            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td style="padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:5px;">
                        <![endif]-->
                        <div class="col num12" style=min-width:320px;max-width:480px;display:table-cell;vertical-align:top;width:480px>
                            <div style=width:100%!important>
                                <!--[if (!mso)&(!IE)]><!-->
                                <div style="border-top:0 solid transparent;border-left:0 solid transparent;border-bottom:0 solid transparent;border-right:0 solid transparent;padding-top:5px;padding-bottom:5px;padding-right:0;padding-left:0">
                                    <!--<![endif]-->
                                    <div align=center class="img-container center fixedwidth" style=padding-right:0;padding-left:0>
                                        <!--[if mso]>
                                        <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                            <tr style="line-height:0px">
                                                <td style="padding-right: 0px;padding-left: 0px;" align="center">
                                        <![endif]-->
                                        <div style=font-size:1px;line-height:32px> </div>
                                        <img align=center alt="I''''m an image" border=0 class="center fixedwidth" src=cid:streem-color style=text-decoration:none;-ms-interpolation-mode:bicubic;height:auto;border:0;width:100%;max-width:96px;display:block title="I''''m an image" width=96>
                                        <div style=font-size:1px;line-height:32px> </div>
                                        <!--[if mso]></td></tr></table><![endif]-->
                                    </div>
                                    <!--[if mso]>
                                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td style="padding-right: 42px; padding-left: 42px; padding-top: 0px; padding-bottom: 32px; font-family: Arial, sans-serif">
                                    <![endif]-->
                                    <div style="color:#555;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;line-height:1.2;padding-top:0;padding-right:42px;padding-bottom:32px;padding-left:42px">
                                        <div style="line-height:1.2;font-size:12px;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;color:#555;mso-line-height-alt:14px">
                                            <p style="font-size:20px;line-height:1.2;text-align:center;word-break:break-word;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:24px;margin:0">
                                                <span style=font-size:20px;color:#000>You are invited to start using CLEEN for digitalising Cleaning</span>
                                            </p>
                                        </div>
                                    </div>
                                    <!--[if mso]></td></tr></table><![endif]-->
                                    <table border=0 cellpadding=0 cellspacing=0 class=divider role=presentation style=table-layout:fixed;vertical-align:top;border-spacing:0;border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;min-width:100%;-ms-text-size-adjust:100%;-webkit-text-size-adjust:100% valign=top width=100%>
                                        <tbody>
                                        <tr style=vertical-align:top valign=top>
                                            <td class=divider_inner style=word-break:break-word;vertical-align:top;min-width:100%;-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%;padding-top:0;padding-right:0;padding-bottom:32px;padding-left:0 valign=top>
                                                <table align=center border=0 cellpadding=0 cellspacing=0 class=divider_content role=presentation style="table-layout:fixed;vertical-align:top;border-spacing:0;border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;border-top:1px solid #eee;width:100%" valign=top width=100%>
                                                    <tbody>
                                                    <tr style=vertical-align:top valign=top>
                                                        <td style=word-break:break-word;vertical-align:top;-ms-text-size-adjust:100%;-webkit-text-size-adjust:100% valign=top><span></span></td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    <!--[if mso]>
                                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td style="padding-right: 32px; padding-left: 32px; padding-top: 0px; padding-bottom: 24px; font-family: Arial, sans-serif">
                                    <![endif]-->
                                    <div style="color:#555;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;line-height:1.5;padding-top:0;padding-right:32px;padding-bottom:24px;padding-left:32px">
                                        <div style="line-height:1.5;font-size:12px;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;color:#555;mso-line-height-alt:18px">
                                            <p style="font-size:14px;line-height:1.5;word-break:break-word;text-align:left;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:21px;margin:0">
                                                <span style=font-size:14px;color:#000>Hello, </span></p>
                                            <p style="font-size:14px;line-height:1.5;word-break:break-word;text-align:left;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:21px;margin:0">
                                                 </p>
                                            <p style="font-size:14px;line-height:1.5;word-break:break-word;text-align:left;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:21px;margin:0">
                                                <span style=font-size:14px;color:#000>Your Company is now using CLEEN to digitalise Cleaning Work Instructions at your Facility. Click the button below to register yourself and get started with CLEEN. </span>
                                            </p>
                                        </div>
                                    </div>
                                    <!--[if mso]></td></tr></table><![endif]-->
                                    <a style=text-decoration:none;cursor:pointer href=${registerlink}>
                                        <div align=left class=button-container style=padding-top:0;padding-right:32px;padding-bottom:24px;padding-left:32px>
                                            <!--[if mso]>
                                            <table width="100%" cellpadding="0" cellspacing="0" border="0"
                                                   style="border-spacing: 0; border-collapse: collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;">
                                                <tr>
                                                    <td style="padding-top: 0px; padding-right: 32px; padding-bottom: 24px; padding-left: 32px"
                                                        align="left">
                                                        <v:roundrect xmlns:v="urn:schemas-microsoft-com:vml"
                                                                     xmlns:w="urn:schemas-microsoft-com:office:word"
                                                                     href=""
                                                                     style="height:33pt; width:170.25pt; v-text-anchor:middle;"
                                                                     arcsize="10%" stroke="false" fillcolor="#1d84ff">
                                                            <w:anchorlock/>
                                                            <v:textbox inset="0,0,0,0">
                                                                <center style="color:#ffffff; font-family:Arial, sans-serif; font-size:14px">
                                            <![endif]-->
                                            <div style="text-decoration:none;display:inline-block;color:#fff;background-color:#1d84ff;border-radius:4px;-webkit-border-radius:4px;-moz-border-radius:4px;width:auto;width:auto;border-top:1px solid #1d84ff;border-right:1px solid #1d84ff;border-bottom:1px solid #1d84ff;border-left:1px solid #1d84ff;padding-top:6px;padding-bottom:6px;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;text-align:center;mso-border-alt:none;word-break:keep-all">
                                                <span style=padding-left:24px;padding-right:24px;font-size:14px;display:inline-block><span style="font-size:16px;margin:0;line-height:2;word-break:break-word;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:32px"><span data-mce-style="font-size: 14px; line-height: 28px;" style=font-size:14px;line-height:28px>Register with CLEEN</span></span></span>
                                            </div>
                                            <!--[if mso]></center></v:textbox></v:roundrect></td></tr></table>
                                            <![endif]-->
                                        </div>
                                    </a>
                                    <div class=mobile_hide>
                                        <!--[if mso]>
                                        <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                            <tr>
                                                <td style="padding-right: 32px; padding-left: 32px; padding-top: 0px; padding-bottom: 32px; font-family: Arial, sans-serif">
                                        <![endif]-->
                                        <div style="color:#393d47;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;line-height:1.2;padding-top:0;padding-right:32px;padding-bottom:32px;padding-left:32px">
                                            <div style="line-height:1.2;font-size:12px;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;color:#393d47;mso-line-height-alt:14px">
                                                <p style="font-size:12px;line-height:1.2;word-break:break-word;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:14px;margin:0">
                                                    <span style=font-size:12px><span style=color:#000>You may copy/paste this link into your web browser -</span> <span style=color:#1d84ff>${registerlink}</span></span></p>
                                            </div>
                                        </div>
                                        <!--[if mso]></td></tr></table><![endif]-->
                                    </div>
                                    <!--[if mso]>
                                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td style="padding-right: 32px; padding-left: 32px; padding-top: 0px; padding-bottom: 60px; font-family: Arial, sans-serif">
                                    <![endif]-->
                                    <div style="color:#393d47;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;line-height:1.5;padding-top:0;padding-right:32px;padding-bottom:60px;padding-left:32px">
                                        <div style="line-height:1.5;font-size:12px;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;color:#393d47;mso-line-height-alt:18px">
                                            <p style="font-size:14px;line-height:1.5;word-break:break-word;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:21px;margin:0">
                                                <span style=color:#000>Warm Regards</span></p>
                                            <p style="font-size:14px;line-height:1.5;word-break:break-word;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:21px;margin:0">
                                                <span style=color:#000>CLEEN App</span></p>
                                        </div>
                                    </div>
                                    <!--[if mso]></td></tr></table><![endif]-->
                                    <!--[if (!mso)&(!IE)]><!-->
                                </div>
                                <!--<![endif]-->
                            </div>
                        </div>
                        <!--[if (mso)|(IE)]></td></tr></table><![endif]-->
                        <!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->
                    </div>
                </div>
            </div>
            <div style=background-color:#f4f4f4>
                <div class=block-grid style="Margin:0 auto;min-width:320px;max-width:480px;overflow-wrap:break-word;word-wrap:break-word;word-break:break-word;background-color:transparent">
                    <div style=border-collapse:collapse;display:table;width:100%;background-color:transparent>
                        <!--[if (mso)|(IE)]>
                        <table width="100%" cellpadding="0" cellspacing="0" border="0"
                               style="background-color:#f4f4f4;">
                            <tr>
                                <td align="center">
                                    <table cellpadding="0" cellspacing="0" border="0" style="width:480px">
                                        <tr class="layout-full-width" style="background-color:transparent"><![endif]-->
                        <!--[if (mso)|(IE)]>
                        <td align="center" width="480"
                            style="background-color:transparent;width:480px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;"
                            valign="top">
                            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td style="padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:5px;">
                        <![endif]-->
                        <div class="col num12" style=min-width:320px;max-width:480px;display:table-cell;vertical-align:top;width:480px>
                            <div style=width:100%!important>
                                <!--[if (!mso)&(!IE)]><!-->
                                <div style="border-top:0 solid transparent;border-left:0 solid transparent;border-bottom:0 solid transparent;border-right:0 solid transparent;padding-top:5px;padding-bottom:5px;padding-right:0;padding-left:0">
                                    <!--<![endif]-->
                                    <div class=mobile_hide>
                                        <!--[if mso]>
                                        <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                            <tr>
                                                <td style="padding-right: 10px; padding-left: 10px; padding-top: 24px; padding-bottom: 0px; font-family: Arial, sans-serif">
                                        <![endif]-->
                                        <div style="color:#393d47;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;line-height:1.2;padding-top:24px;padding-right:10px;padding-bottom:0;padding-left:10px">
                                            <div style="line-height:1.2;font-size:12px;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;color:#393d47;mso-line-height-alt:14px">
                                                <p style="font-size:12px;line-height:1.2;word-break:break-word;text-align:center;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:14px;margin:0">
                                                    <span style=font-size:12px;color:#000>To login to the CLEEN App open this link in a web browser (e.g. Google Chrome) - <span style=color:#1d84ff>${streemlogin}</span></span></p>
                                            </div>
                                        </div>
                                        <!--[if mso]></td></tr></table><![endif]-->
                                    </div>
                                    <!--[if (!mso)&(!IE)]><!-->
                                </div>
                                <!--<![endif]-->
                            </div>
                        </div>
                        <!--[if (mso)|(IE)]></td></tr></table><![endif]-->
                        <!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->
                    </div>
                </div>
            </div>
            <!--[if (mso)|(IE)]></td></tr></table><![endif]-->
        </td>
    </tr>
    </tbody>
</table>
<!--[if (IE)]></div><![endif]-->
</body>
</html>');

--changeset sathyam:04-seed-data-2
INSERT INTO email_templates (id, name, content)
VALUES (2, 'REMINDER_TO_REGISTER', '
<!doctype html>
<html xmlns=http://www.w3.org/1999/xhtml xmlns:o=urn:schemas-microsoft-com:office:office xmlns:v=urn:schemas-microsoft-com:vml xmlns:th=http://www.w3.org/1999/xhtml>
<head>
    <!--[if gte mso 9]><xml><o:OfficeDocumentSettings><o:AllowPNG/><o:PixelsPerInch>96</o:PixelsPerInch></o:OfficeDocumentSettings></xml><![endif]-->
    <meta content="text/html; charset=utf-8" http-equiv=Content-Type>
    <meta content="width=device-width" name=viewport>
    <!--[if !mso]><!-->
    <meta content="IE=edge" http-equiv=X-UA-Compatible>
    <!--<![endif]-->
    <title></title>
    <!--[if !mso]><!-->
    <link href="https://fonts.googleapis.com/css?family=Open+Sans" rel=stylesheet>
    <link href="https://fonts.googleapis.com/css?family=Lato" rel=stylesheet>
    <link href="https://fonts.googleapis.com/css?family=Nunito" rel=stylesheet>
    <!--<![endif]-->
    <style>body{margin:0;padding:0}table,td,tr{vertical-align:top;border-collapse:collapse}*{line-height:inherit}a[x-apple-data-detectors=true]{color:inherit!important;text-decoration:none!important}</style>
    <style id=media-query>@media (max-width:500px){.block-grid,.col{min-width:320px!important;max-width:100%!important;display:block!important}.block-grid{width:100%!important}.col{width:100%!important}.col>div{margin:0 auto}img.fullwidth,img.fullwidthOnMobile{max-width:100%!important}.no-stack .col{min-width:0!important;display:table-cell!important}.no-stack.two-up .col{width:50%!important}.no-stack .col.num4{width:33%!important}.no-stack .col.num8{width:66%!important}.no-stack .col.num4{width:33%!important}.no-stack .col.num3{width:25%!important}.no-stack .col.num6{width:50%!important}.no-stack .col.num9{width:75%!important}.video-block{max-width:none!important}.mobile_hide{min-height:0;max-height:0;max-width:0;display:none;overflow:hidden;font-size:0}.desktop_hide{display:block!important;max-height:none!important}}</style>
</head>
<body class=clean-body style=margin:0;padding:0;-webkit-text-size-adjust:100%;background-color:#fff>
<!--[if IE]><div class="ie-browser"><![endif]-->
<table bgcolor=#FFFFFF cellpadding=0 cellspacing=0 class=nl-container role=presentation style="table-layout:fixed;vertical-align:top;min-width:320px;Margin:0 auto;border-spacing:0;border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;background-color:#fff;width:100%" valign=top width=100%>
    <tbody>
    <tr style=vertical-align:top valign=top>
        <td style=word-break:break-word;vertical-align:top valign=top>
            <!--[if (mso)|(IE)]><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td align="center" style="background-color:#FFFFFF"><![endif]-->
            <div style=background-color:#f4f4f4>
                <div class=block-grid style="Margin:0 auto;min-width:320px;max-width:480px;overflow-wrap:break-word;word-wrap:break-word;word-break:break-word;background-color:transparent">
                    <div style=border-collapse:collapse;display:table;width:100%;background-color:transparent>
                        <!--[if (mso)|(IE)]><table width="100%" cellpadding="0" cellspacing="0" border="0" style="background-color:#f4f4f4;"><tr><td align="center"><table cellpadding="0" cellspacing="0" border="0" style="width:480px"><tr class="layout-full-width" style="background-color:transparent"><![endif]-->
                        <!--[if (mso)|(IE)]><td align="center" width="480" style="background-color:transparent;width:480px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;" valign="top"><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td style="padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:5px;"><![endif]-->
                        <div class="col num12" style=min-width:320px;max-width:480px;display:table-cell;vertical-align:top;width:480px>
                            <div style=width:100%!important>
                                <!--[if (!mso)&(!IE)]><!-->
                                <div style="border-top:0 solid transparent;border-left:0 solid transparent;border-bottom:0 solid transparent;border-right:0 solid transparent;padding-top:5px;padding-bottom:5px;padding-right:0;padding-left:0">
                                    <!--<![endif]-->
                                    <div></div>
                                    <!--[if (!mso)&(!IE)]><!-->
                                </div>
                                <!--<![endif]-->
                            </div>
                        </div>
                        <!--[if (mso)|(IE)]></td></tr></table><![endif]-->
                        <!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->
                    </div>
                </div>
            </div>
            <div style=background-color:#f4f4f4>
                <div class=block-grid style="Margin:0 auto;min-width:320px;max-width:480px;overflow-wrap:break-word;word-wrap:break-word;word-break:break-word;background-color:#fff">
                    <div style=border-collapse:collapse;display:table;width:100%;background-color:#fff>
                        <!--[if (mso)|(IE)]><table width="100%" cellpadding="0" cellspacing="0" border="0" style="background-color:#f4f4f4;"><tr><td align="center"><table cellpadding="0" cellspacing="0" border="0" style="width:480px"><tr class="layout-full-width" style="background-color:#ffffff"><![endif]-->
                        <!--[if (mso)|(IE)]><td align="center" width="480" style="background-color:#ffffff;width:480px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;" valign="top"><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td style="padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:5px;"><![endif]-->
                        <div class="col num12" style=min-width:320px;max-width:480px;display:table-cell;vertical-align:top;width:480px>
                            <div style=width:100%!important>
                                <!--[if (!mso)&(!IE)]><!-->
                                <a style="border-top:0 solid transparent;border-left:0 solid transparent;border-bottom:0 solid transparent;border-right:0 solid transparent;padding-top:5px;padding-bottom:5px;padding-right:0;padding-left:0">
                                    <!--<![endif]-->
                                    <div align=center class="img-container center fixedwidth" style=padding-right:0;padding-left:0>
                                        <!--[if mso]><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr style="line-height:0px"><td style="padding-right: 0px;padding-left: 0px;" align="center"><![endif]-->
                                        <div style=font-size:1px;line-height:32px> </div><img align=center alt="I''''m an image" border=0 class="center fixedwidth" src=cid:streem-color style=text-decoration:none;-ms-interpolation-mode:bicubic;height:auto;border:0;width:100%;max-width:96px;display:block title="I''''m an image" width=96>
                                        <div style=font-size:1px;line-height:32px> </div>
                                        <!--[if mso]></td></tr></table><![endif]-->
                                    </div>
                                    <!--[if mso]><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td style="padding-right: 42px; padding-left: 42px; padding-top: 0px; padding-bottom: 32px; font-family: Arial, sans-serif"><![endif]-->
                                    <div style="color:#555;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;line-height:1.2;padding-top:0;padding-right:42px;padding-bottom:32px;padding-left:42px">
                                        <div style="line-height:1.2;font-size:12px;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;color:#555;mso-line-height-alt:14px">
                                            <p style="font-size:20px;line-height:1.2;text-align:center;word-break:break-word;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:24px;margin:0"><span style=font-size:20px;color:#000>A gentle reminder to complete your </span><span style=font-size:20px;color:#000>registration with CLEEN</span></p>
                                        </div>
                                    </div>
                                    <!--[if mso]></td></tr></table><![endif]-->
                                    <table border=0 cellpadding=0 cellspacing=0 class=divider role=presentation style=table-layout:fixed;vertical-align:top;border-spacing:0;border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;min-width:100%;-ms-text-size-adjust:100%;-webkit-text-size-adjust:100% valign=top width=100%>
                                        <tbody>
                                        <tr style=vertical-align:top valign=top>
                                            <td class=divider_inner style=word-break:break-word;vertical-align:top;min-width:100%;-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%;padding-top:0;padding-right:0;padding-bottom:32px;padding-left:0 valign=top>
                                                <table align=center border=0 cellpadding=0 cellspacing=0 class=divider_content role=presentation style="table-layout:fixed;vertical-align:top;border-spacing:0;border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;border-top:1px solid #eee;width:100%" valign=top width=100%>
                                                    <tbody>
                                                    <tr style=vertical-align:top valign=top>
                                                        <td style=word-break:break-word;vertical-align:top;-ms-text-size-adjust:100%;-webkit-text-size-adjust:100% valign=top><span></span></td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    <!--[if mso]><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td style="padding-right: 32px; padding-left: 32px; padding-top: 0px; padding-bottom: 24px; font-family: Arial, sans-serif"><![endif]-->
                                    <div style="color:#555;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;line-height:1.5;padding-top:0;padding-right:32px;padding-bottom:24px;padding-left:32px">
                                        <div style="line-height:1.5;font-size:12px;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;color:#555;mso-line-height-alt:18px">
                                            <p style="font-size:14px;line-height:1.5;word-break:break-word;text-align:left;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:21px;margin:0"><span style=font-size:14px;color:#000>Hello, </span></p>
                                            <p style="font-size:14px;line-height:1.5;word-break:break-word;text-align:left;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:21px;margin:0"> </p>
                                            <p style="font-size:14px;line-height:1.5;word-break:break-word;text-align:left;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:21px;margin:0"><span style=font-size:14px;color:#000>Your registration is still pending. Please click the button below to complete your it and get started CLEEN</span></p>
                                        </div>
                                    </div>
                                    <!--[if mso]></td></tr></table><![endif]-->
                                    <a href=${registerlink} style=text-decoration:none;cursor:pointer>
                                        <div align=left class=button-container style=padding-top:0;padding-right:32px;padding-bottom:24px;padding-left:32px>
                                            <!--[if mso]><table width="100%" cellpadding="0" cellspacing="0" border="0" style="border-spacing: 0; border-collapse: collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;"><tr><td style="padding-top: 0px; padding-right: 32px; padding-bottom: 24px; padding-left: 32px" align="left"><v:roundrect xmlns:v="urn:schemas-microsoft-com:vml" xmlns:w="urn:schemas-microsoft-com:office:word" href="" style="height:33pt; width:170.25pt; v-text-anchor:middle;" arcsize="10%" stroke="false" fillcolor="#1d84ff"><w:anchorlock/><v:textbox inset="0,0,0,0"><center style="color:#ffffff; font-family:Arial, sans-serif; font-size:14px"><![endif]-->
                                            <div style="text-decoration:none;display:inline-block;color:#fff;background-color:#1d84ff;border-radius:4px;-webkit-border-radius:4px;-moz-border-radius:4px;width:auto;width:auto;border-top:1px solid #1d84ff;border-right:1px solid #1d84ff;border-bottom:1px solid #1d84ff;border-left:1px solid #1d84ff;padding-top:6px;padding-bottom:6px;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;text-align:center;mso-border-alt:none;word-break:keep-all"><span style=padding-left:24px;padding-right:24px;font-size:14px;display:inline-block><span style="font-size:16px;margin:0;line-height:2;word-break:break-word;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:32px"><span data-mce-style="font-size: 14px; line-height: 28px;" style=font-size:14px;line-height:28px>Register with CLEEN</span></span></span></div>
                                            <!--[if mso]></center></v:textbox></v:roundrect></td></tr></table><![endif]-->
                                        </div>
                                    </a>
                                    <div class=mobile_hide>
                                        <!--[if mso]><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td style="padding-right: 32px; padding-left: 32px; padding-top: 0px; padding-bottom: 32px; font-family: Arial, sans-serif"><![endif]-->
                                        <div style="color:#393d47;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;line-height:1.2;padding-top:0;padding-right:32px;padding-bottom:32px;padding-left:32px">
                                            <div style="line-height:1.2;font-size:12px;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;color:#393d47;mso-line-height-alt:14px">
                                                <p style="font-size:12px;line-height:1.2;word-break:break-word;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:14px;margin:0"><span style=font-size:12px><span style=color:#000>You may copy/paste this link into your web browser -</span> <span style=color:#1d84ff>${registerlink}</span></span></p>
                                            </div>
                                        </div>
                                        <!--[if mso]></td></tr></table><![endif]-->
                                    </div>
                                    <!--[if mso]><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td style="padding-right: 32px; padding-left: 32px; padding-top: 0px; padding-bottom: 60px; font-family: Arial, sans-serif"><![endif]-->
                                    <div style="color:#393d47;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;line-height:1.5;padding-top:0;padding-right:32px;padding-bottom:60px;padding-left:32px">
                                        <div style="line-height:1.5;font-size:12px;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;color:#393d47;mso-line-height-alt:18px">
                                            <p style="font-size:14px;line-height:1.5;word-break:break-word;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:21px;margin:0"><span style=color:#000>Warm Regards</span></p>
                                            <p style="font-size:14px;line-height:1.5;word-break:break-word;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:21px;margin:0"><span style=color:#000>CLEEN App</span></p>
                                        </div>
                                    </div>
                                    <!--[if mso]></td></tr></table><![endif]-->
                                    <!--[if (!mso)&(!IE)]><!-->
                                </a></div>
                            <!--<![endif]-->
                        </div>
                    </div>
                    <!--[if (mso)|(IE)]></td></tr></table><![endif]-->
                    <!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->
                </div>
            </div>

            <div style=background-color:#f4f4f4>
                <div class=block-grid style="Margin:0 auto;min-width:320px;max-width:480px;overflow-wrap:break-word;word-wrap:break-word;word-break:break-word;background-color:transparent">
                    <div style=border-collapse:collapse;display:table;width:100%;background-color:transparent>
                        <!--[if (mso)|(IE)]><table width="100%" cellpadding="0" cellspacing="0" border="0" style="background-color:#f4f4f4;"><tr><td align="center"><table cellpadding="0" cellspacing="0" border="0" style="width:480px"><tr class="layout-full-width" style="background-color:transparent"><![endif]-->
                        <!--[if (mso)|(IE)]><td align="center" width="480" style="background-color:transparent;width:480px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;" valign="top"><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td style="padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:5px;"><![endif]-->
                        <div class="col num12" style=min-width:320px;max-width:480px;display:table-cell;vertical-align:top;width:480px>
                            <div style=width:100%!important>
                                <!--[if (!mso)&(!IE)]><!-->
                                <div style="border-top:0 solid transparent;border-left:0 solid transparent;border-bottom:0 solid transparent;border-right:0 solid transparent;padding-top:5px;padding-bottom:5px;padding-right:0;padding-left:0">
                                    <!--<![endif]-->
                                    <div class=mobile_hide>
                                        <!--[if mso]><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td style="padding-right: 10px; padding-left: 10px; padding-top: 24px; padding-bottom: 0px; font-family: Arial, sans-serif"><![endif]-->
                                        <div style="color:#393d47;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;line-height:1.2;padding-top:24px;padding-right:10px;padding-bottom:0;padding-left:10px">
                                            <div style="line-height:1.2;font-size:12px;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;color:#393d47;mso-line-height-alt:14px">
                                                <p style="font-size:12px;line-height:1.2;word-break:break-word;text-align:center;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:14px;margin:0"><span style=font-size:12px;color:#000>To login to the CLEEN App open this link in a web browser (e.g. Google Chrome) - <span style=color:#1d84ff>${streemlogin}</span></span></p>
                                            </div>
                                        </div>
                                        <!--[if mso]></td></tr></table><![endif]-->
                                    </div>
                                    <!--[if (!mso)&(!IE)]><!-->
                                </div>
                                <!--<![endif]-->
                            </div>
                        </div>
                        <!--[if (mso)|(IE)]></td></tr></table><![endif]-->
                        <!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->
                    </div>
                </div>
            </div>
            <!--[if (mso)|(IE)]></td></tr></table><![endif]-->
        </td>
    </tr>
    </tbody>
</table>
<!--[if (IE)]></div><![endif]-->
</body>
</html>');

--changeset sathyam:04-seed-data-3
INSERT INTO email_templates (id, name, content)
VALUES (3, 'RESET_PASSWORD', '
<!doctype html>
<html xmlns=http://www.w3.org/1999/xhtml xmlns:th=http://www.w3.org/1999/xhtml>
<head>
    <!--[if gte mso 9]>
    <xml>
        <o:OfficeDocumentSettings>
            <o:AllowPNG/>
            <o:PixelsPerInch>96</o:PixelsPerInch>
        </o:OfficeDocumentSettings>
    </xml><![endif]-->
    <meta content="text/html; charset=utf-8" http-equiv=Content-Type>
    <meta content="width=device-width" name=viewport>
    <!--[if !mso]><!-->
    <meta content="IE=edge" http-equiv=X-UA-Compatible>
    <!--<![endif]-->
    <title></title>
    <!--[if !mso]><!-->
    <link href="https://fonts.googleapis.com/css?family=Open+Sans" rel=stylesheet>
    <link href="https://fonts.googleapis.com/css?family=Lato" rel=stylesheet>
    <link href="https://fonts.googleapis.com/css?family=Nunito" rel=stylesheet>
    <!--<![endif]-->
    <style>body{margin:0;padding:0}table,td,tr{vertical-align:top;border-collapse:collapse}*{line-height:inherit}a[x-apple-data-detectors=true]{color:inherit!important;text-decoration:none!important}</style>
    <style id=media-query>@media (max-width:500px){.block-grid,.col{min-width:320px!important;max-width:100%!important;display:block!important}.block-grid{width:100%!important}.col{width:100%!important}.col>div{margin:0 auto}img.fullwidth,img.fullwidthOnMobile{max-width:100%!important}.no-stack .col{min-width:0!important;display:table-cell!important}.no-stack.two-up .col{width:50%!important}.no-stack .col.num4{width:33%!important}.no-stack .col.num8{width:66%!important}.no-stack .col.num4{width:33%!important}.no-stack .col.num3{width:25%!important}.no-stack .col.num6{width:50%!important}.no-stack .col.num9{width:75%!important}.video-block{max-width:none!important}.mobile_hide{min-height:0;max-height:0;max-width:0;display:none;overflow:hidden;font-size:0}.desktop_hide{display:block!important;max-height:none!important}}</style>
</head>
<body class=clean-body style=margin:0;padding:0;-webkit-text-size-adjust:100%;background-color:#fff>
<!--[if IE]>
<div class="ie-browser"><![endif]-->
<table bgcolor=#FFFFFF cellpadding=0 cellspacing=0 class=nl-container role=presentation style="table-layout:fixed;vertical-align:top;min-width:320px;Margin:0 auto;border-spacing:0;border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;background-color:#fff;width:100%" valign=top width=100%>
    <tbody>
    <tr style=vertical-align:top valign=top>
        <td style=word-break:break-word;vertical-align:top valign=top>
            <!--[if (mso)|(IE)]>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td align="center" style="background-color:#FFFFFF"><![endif]-->
            <div style=background-color:#f4f4f4>
                <div class=block-grid style="Margin:0 auto;min-width:320px;max-width:480px;overflow-wrap:break-word;word-wrap:break-word;word-break:break-word;background-color:transparent">
                    <div style=border-collapse:collapse;display:table;width:100%;background-color:transparent>
                        <!--[if (mso)|(IE)]>
                        <table width="100%" cellpadding="0" cellspacing="0" border="0"
                               style="background-color:#f4f4f4;">
                            <tr>
                                <td align="center">
                                    <table cellpadding="0" cellspacing="0" border="0" style="width:480px">
                                        <tr class="layout-full-width" style="background-color:transparent"><![endif]-->
                        <!--[if (mso)|(IE)]>
                        <td align="center" width="480"
                            style="background-color:transparent;width:480px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;"
                            valign="top">
                            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td style="padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:5px;">
                        <![endif]-->
                        <div class="col num12" style=min-width:320px;max-width:480px;display:table-cell;vertical-align:top;width:480px>
                            <div style=width:100%!important>
                                <!--[if (!mso)&(!IE)]><!-->
                                <div style="border-top:0 solid transparent;border-left:0 solid transparent;border-bottom:0 solid transparent;border-right:0 solid transparent;padding-top:5px;padding-bottom:5px;padding-right:0;padding-left:0">
                                    <!--<![endif]-->
                                    <div></div>
                                    <!--[if (!mso)&(!IE)]><!-->
                                </div>
                                <!--<![endif]-->
                            </div>
                        </div>
                        <!--[if (mso)|(IE)]></td></tr></table><![endif]-->
                        <!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->
                    </div>
                </div>
            </div>
            <div style=background-color:#f4f4f4>
                <div class=block-grid style="Margin:0 auto;min-width:320px;max-width:480px;overflow-wrap:break-word;word-wrap:break-word;word-break:break-word;background-color:#fff">
                    <div style=border-collapse:collapse;display:table;width:100%;background-color:#fff>
                        <!--[if (mso)|(IE)]>
                        <table width="100%" cellpadding="0" cellspacing="0" border="0"
                               style="background-color:#f4f4f4;">
                            <tr>
                                <td align="center">
                                    <table cellpadding="0" cellspacing="0" border="0" style="width:480px">
                                        <tr class="layout-full-width" style="background-color:#ffffff"><![endif]-->
                        <!--[if (mso)|(IE)]>
                        <td align="center" width="480"
                            style="background-color:#ffffff;width:480px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;"
                            valign="top">
                            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td style="padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:5px;">
                        <![endif]-->
                        <div class="col num12" style=min-width:320px;max-width:480px;display:table-cell;vertical-align:top;width:480px>
                            <div style=width:100%!important>
                                <!--[if (!mso)&(!IE)]><!-->
                                <div style="border-top:0 solid transparent;border-left:0 solid transparent;border-bottom:0 solid transparent;border-right:0 solid transparent;padding-top:5px;padding-bottom:5px;padding-right:0;padding-left:0">
                                    <!--<![endif]-->
                                    <div align=center class="img-container center fixedwidth" style=padding-right:0;padding-left:0>
                                        <!--[if mso]>
                                        <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                            <tr style="line-height:0px">
                                                <td style="padding-right: 0px;padding-left: 0px;" align="center">
                                        <![endif]-->
                                        <div style=font-size:1px;line-height:32px> </div>
                                        <img align=center alt="I''''m an image" border=0 class="center fixedwidth" src=cid:streem-color style=text-decoration:none;-ms-interpolation-mode:bicubic;height:auto;border:0;width:100%;max-width:96px;display:block title="I''''m an image" width=96>
                                        <div style=font-size:1px;line-height:32px> </div>
                                        <!--[if mso]></td></tr></table><![endif]-->
                                    </div>
                                    <!--[if mso]>
                                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td style="padding-right: 42px; padding-left: 42px; padding-top: 0px; padding-bottom: 32px; font-family: Arial, sans-serif">
                                    <![endif]-->
                                    <div style="color:#555;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;line-height:1.2;padding-top:0;padding-right:42px;padding-bottom:32px;padding-left:42px">
                                        <div style="line-height:1.2;font-size:12px;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;color:#555;mso-line-height-alt:14px">
                                            <p style="font-size:16px;line-height:1.2;text-align:center;word-break:break-word;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:19px;margin:0">
                                                <span style=color:#000><span style=font-size:20px>You have requested to reset your Password</span></span>
                                            </p>
                                        </div>
                                    </div>
                                    <!--[if mso]></td></tr></table><![endif]-->
                                    <table border=0 cellpadding=0 cellspacing=0 class=divider role=presentation style=table-layout:fixed;vertical-align:top;border-spacing:0;border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;min-width:100%;-ms-text-size-adjust:100%;-webkit-text-size-adjust:100% valign=top width=100%>
                                        <tbody>
                                        <tr style=vertical-align:top valign=top>
                                            <td class=divider_inner style=word-break:break-word;vertical-align:top;min-width:100%;-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%;padding-top:0;padding-right:0;padding-bottom:32px;padding-left:0 valign=top>
                                                <table align=center border=0 cellpadding=0 cellspacing=0 class=divider_content role=presentation style="table-layout:fixed;vertical-align:top;border-spacing:0;border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;border-top:1px solid #eee;width:100%" valign=top width=100%>
                                                    <tbody>
                                                    <tr style=vertical-align:top valign=top>
                                                        <td style=word-break:break-word;vertical-align:top;-ms-text-size-adjust:100%;-webkit-text-size-adjust:100% valign=top><span></span></td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    <!--[if mso]>
                                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td style="padding-right: 32px; padding-left: 32px; padding-top: 0px; padding-bottom: 24px; font-family: Arial, sans-serif">
                                    <![endif]-->
                                    <div style="color:#555;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;line-height:1.5;padding-top:0;padding-right:32px;padding-bottom:24px;padding-left:32px">
                                        <div style="line-height:1.5;font-size:12px;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;color:#555;mso-line-height-alt:18px">
                                            <p style="font-size:14px;line-height:1.5;word-break:break-word;text-align:left;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:21px;margin:0">
                                                <span style=font-size:14px;color:#000>Hello, </span></p>
                                            <p style="font-size:14px;line-height:1.5;word-break:break-word;text-align:left;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:21px;margin:0">
                                                 </p>
                                            <p style="line-height:1.5;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;word-break:break-word;font-size:14px;mso-line-height-alt:21px;margin:0">
                                                <span style=font-size:14px;color:#000>You have indicated that you have forgotten your Account Password for CLEEN. If you really did then click the button below to set a new Password. </span>
                                            </p>
                                            <p style="line-height:1.5;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;word-break:break-word;mso-line-height-alt:18px;margin:0">
                                                 </p>
                                            <p style="line-height:1.5;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;word-break:break-word;font-size:14px;mso-line-height-alt:21px;margin:0">
                                                <span style=font-size:14px;color:#000>If you didn''''t mean to reset your Password, then you can simply ignore this email. You Password will not change and you can continue using the current Password.</span>
                                            </p>
                                        </div>
                                    </div>
                                    <!--[if mso]></td></tr></table><![endif]-->
                                    <a style=text-decoration:none;cursor:pointer href=${registerlink}>
                                        <div align=left class=button-container style=padding-top:0;padding-right:32px;padding-bottom:24px;padding-left:32px>
                                            <!--[if mso]>
                                            <table width="100%" cellpadding="0" cellspacing="0" border="0"
                                                   style="border-spacing: 0; border-collapse: collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;">
                                                <tr>
                                                    <td style="padding-top: 0px; padding-right: 32px; padding-bottom: 24px; padding-left: 32px"
                                                        align="left">
                                                        <v:roundrect xmlns:v="urn:schemas-microsoft-com:vml"
                                                                     xmlns:w="urn:schemas-microsoft-com:office:word"
                                                                     href=""
                                                                     style="height:33pt; width:170.25pt; v-text-anchor:middle;"
                                                                     arcsize="10%" stroke="false" fillcolor="#1d84ff">
                                                            <w:anchorlock/>
                                                            <v:textbox inset="0,0,0,0">
                                                                <center style="color:#ffffff; font-family:Arial, sans-serif; font-size:14px">
                                            <![endif]-->
                                            <div style="text-decoration:none;display:inline-block;color:#fff;background-color:#1d84ff;border-radius:4px;-webkit-border-radius:4px;-moz-border-radius:4px;width:auto;width:auto;border-top:1px solid #1d84ff;border-right:1px solid #1d84ff;border-bottom:1px solid #1d84ff;border-left:1px solid #1d84ff;padding-top:6px;padding-bottom:6px;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;text-align:center;mso-border-alt:none;word-break:keep-all">
                                                <span style=padding-left:24px;padding-right:24px;font-size:14px;display:inline-block><span style="font-size:16px;margin:0;line-height:2;word-break:break-word;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:32px"><span data-mce-style="font-size: 14px; line-height: 28px;" style=font-size:14px;line-height:28px>Set a new Password</span></span></span>
                                            </div>
                                            <!--[if mso]></center></v:textbox></v:roundrect></td></tr></table>
                                            <![endif]-->
                                        </div>
                                    </a>
                                    <div class=mobile_hide>
                                        <!--[if mso]>
                                        <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                            <tr>
                                                <td style="padding-right: 32px; padding-left: 32px; padding-top: 0px; padding-bottom: 32px; font-family: Arial, sans-serif">
                                        <![endif]-->
                                        <div style="color:#393d47;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;line-height:1.2;padding-top:0;padding-right:32px;padding-bottom:32px;padding-left:32px">
                                            <div style="line-height:1.2;font-size:12px;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;color:#393d47;mso-line-height-alt:14px">
                                                <p style="font-size:12px;line-height:1.2;word-break:break-word;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:14px;margin:0">
                                                    <span style=font-size:12px><span style=color:#000>You may copy/paste this link into your web browser -</span> <span style=color:#1d84ff>${registerlink}</span></span></p>
                                            </div>
                                        </div>
                                        <!--[if mso]></td></tr></table><![endif]-->
                                    </div>
                                    <!--[if mso]>
                                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td style="padding-right: 32px; padding-left: 32px; padding-top: 0px; padding-bottom: 60px; font-family: Arial, sans-serif">
                                    <![endif]-->
                                    <div style="color:#393d47;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;line-height:1.5;padding-top:0;padding-right:32px;padding-bottom:60px;padding-left:32px">
                                        <div style="line-height:1.5;font-size:12px;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;color:#393d47;mso-line-height-alt:18px">
                                            <p style="font-size:14px;line-height:1.5;word-break:break-word;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:21px;margin:0">
                                                <span style=color:#000>Warm Regards</span></p>
                                            <p style="font-size:14px;line-height:1.5;word-break:break-word;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:21px;margin:0">
                                                <span style=color:#000>CLEEN App</span></p>
                                        </div>
                                    </div>
                                    <!--[if mso]></td></tr></table><![endif]-->
                                    <!--[if (!mso)&(!IE)]><!-->
                                </div>
                                <!--<![endif]-->
                            </div>
                        </div>
                        <!--[if (mso)|(IE)]></td></tr></table><![endif]-->
                        <!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->
                    </div>
                </div>
            </div>
            <div style=background-color:#f4f4f4>
                <div class=block-grid style="Margin:0 auto;min-width:320px;max-width:480px;overflow-wrap:break-word;word-wrap:break-word;word-break:break-word;background-color:transparent">
                    <div style=border-collapse:collapse;display:table;width:100%;background-color:transparent>
                        <!--[if (mso)|(IE)]>
                        <table width="100%" cellpadding="0" cellspacing="0" border="0"
                               style="background-color:#f4f4f4;">
                            <tr>
                                <td align="center">
                                    <table cellpadding="0" cellspacing="0" border="0" style="width:480px">
                                        <tr class="layout-full-width" style="background-color:transparent"><![endif]-->
                        <!--[if (mso)|(IE)]>
                        <td align="center" width="480"
                            style="background-color:transparent;width:480px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;"
                            valign="top">
                            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td style="padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:5px;">
                        <![endif]-->
                        <div class="col num12" style=min-width:320px;max-width:480px;display:table-cell;vertical-align:top;width:480px>
                            <div style=width:100%!important>
                                <!--[if (!mso)&(!IE)]><!-->
                                <div style="border-top:0 solid transparent;border-left:0 solid transparent;border-bottom:0 solid transparent;border-right:0 solid transparent;padding-top:5px;padding-bottom:5px;padding-right:0;padding-left:0">
                                    <!--<![endif]-->
                                    <div class=mobile_hide>
                                        <!--[if mso]>
                                        <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                            <tr>
                                                <td style="padding-right: 10px; padding-left: 10px; padding-top: 24px; padding-bottom: 0px; font-family: Arial, sans-serif">
                                        <![endif]-->
                                        <div style="color:#393d47;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;line-height:1.2;padding-top:24px;padding-right:10px;padding-bottom:0;padding-left:10px">
                                            <div style="line-height:1.2;font-size:12px;font-family:''''Nunito'''',Arial,''''Helvetica Neue'''',Helvetica,sans-serif;color:#393d47;mso-line-height-alt:14px">
                                                <p style="font-size:12px;line-height:1.2;word-break:break-word;text-align:center;font-family:Nunito,Arial,''''Helvetica Neue'''',Helvetica,sans-serif;mso-line-height-alt:14px;margin:0">
                                                    <span style=font-size:12px;color:#000>To login to the CLEEN App open this link in a web browser (e.g. Google Chrome) - <span style=color:#1d84ff>${streemlogin}</span></span></p>
                                            </div>
                                        </div>
                                        <!--[if mso]></td></tr></table><![endif]-->
                                    </div>
                                    <!--[if (!mso)&(!IE)]><!-->
                                </div>
                                <!--<![endif]-->
                            </div>
                        </div>
                        <!--[if (mso)|(IE)]></td></tr></table><![endif]-->
                        <!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->
                    </div>
                </div>
            </div>
            <!--[if (mso)|(IE)]></td></tr></table><![endif]-->
        </td>
    </tr>
    </tbody>
</table>
<!--[if (IE)]></div><![endif]-->
</body>
</html>');