; Script generated by the HM NIS Edit Script Wizard.

; HM NIS Edit Wizard helper defines
!define PRODUCT_NAME "BangolfResultat"
!define PRODUCT_VERSION "0.6.7b"
!define PRODUCT_PUBLISHER "Magnus Nilsson"
!define PRODUCT_WEB_SITE "http://web.telia.com/~u44802129/"
!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"
!define PRODUCT_UNINST_ROOT_KEY "HKLM"
!define PRODUCT_STARTMENU_REGVAL "NSIS:StartMenuDir"

; MUI 1.67 compatible ------
!include "MUI.nsh"

; MUI Settings
!define MUI_ABORTWARNING
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install-blue.ico"
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall-blue.ico"

; Welcome page
!define MUI_WELCOMEPAGE_TEXT "Denna guide tar dig igenom installationen av ${PRODUCT_NAME} ${PRODUCT_VERSION}.\r\n\r\nDet rekommenderas att du avslutar alla andra program innan du forts�tter installationen. Detta till�ter att installationen uppdaterar n�dv�ndiga systemfiler utan att beh�va starta om din dator.\r\n\r\nOm du redan har en version av ${PRODUCT_NAME} installerad p� datorn b�r du s�kerhetskopiera filerna i mappen data innan du forts�tter med installationen.\r\n\r\n$_CLICK"
!insertmacro MUI_PAGE_WELCOME
; License page
!insertmacro MUI_PAGE_LICENSE "E:\Java\eclipse\workspace\BangolfResultat\doc\licens.txt"
; Directory page
!insertmacro MUI_PAGE_DIRECTORY
; Start menu page
var ICONS_GROUP
!define MUI_STARTMENUPAGE_NODISABLE
!define MUI_STARTMENUPAGE_DEFAULTFOLDER "${PRODUCT_NAME}"
!define MUI_STARTMENUPAGE_REGISTRY_ROOT "${PRODUCT_UNINST_ROOT_KEY}"
!define MUI_STARTMENUPAGE_REGISTRY_KEY "${PRODUCT_UNINST_KEY}"
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "${PRODUCT_STARTMENU_REGVAL}"
!insertmacro MUI_PAGE_STARTMENU Application $ICONS_GROUP
; Instfiles page
!insertmacro MUI_PAGE_INSTFILES
; Finish page
!define MUI_FINISHPAGE_SHOWREADME "$INSTDIR\doc\versionhistory.htm"
!insertmacro MUI_PAGE_FINISH

; Uninstaller pages
!insertmacro MUI_UNPAGE_INSTFILES

; Language files
!insertmacro MUI_LANGUAGE "Swedish"

; MUI end ------

Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
OutFile "installer\${PRODUCT_NAME} ${PRODUCT_VERSION}.exe"
InstallDir "$PROGRAMFILES\${PRODUCT_NAME}"
ShowInstDetails show
ShowUnInstDetails show

Section "MainSection" SEC01
  SetOutPath "$INSTDIR"
  SetOverwrite ifnewer
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\licens.txt"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\BangolfResultat.jar"
  SetOutPath "$INSTDIR\doc\bilder"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\bilder\align.gif"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\bilder\bgr.gif"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\bilder\bytanamn.gif"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\bilder\indata.gif"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\bilder\jmf.gif"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\bilder\jmfres.gif"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\bilder\jmfsurface.gif"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\bilder\klasser.gif"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\bilder\klasstar.gif"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\bilder\redig.gif"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\bilder\resinmat.gif"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\bilder\resultat.gif"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\bilder\rubrik.gif"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\bilder\save.gif"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\bilder\snitt.gif"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\bilder\sokning.gif"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\bilder\sort.gif"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\bilder\status.gif"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\bilder\utseende.gif"
  SetOutPath "$INSTDIR\doc"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\backup.htm"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\comp.htm"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\manual.htm"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\om.htm"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\snitt.htm"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\snittlista.htm"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\sok.htm"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\system.htm"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\versionhistory.htm"
  File "E:\Java\eclipse\workspace\BangolfResultat\doc\webbsida.htm"
  SetOutPath "$INSTDIR\data"
  SetOverwrite off
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\brikon.gif"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\classorder"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\compare"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\compareby"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\comparefiles"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\directory"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\dirhtm"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\dirjmf"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\dirskv"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\dirsnitt"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\ikoner.icl"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\klass"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\klassmap"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\klasstring"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\licensemap"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\licensenamemap"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\namn"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\orientation"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\pnametrack"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\ptrack"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\snitt"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\snittapp"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\snittmap"
  File "E:\Java\eclipse\workspace\BangolfResultat\installer\data\snittstring"
SectionEnd

Section -AdditionalIcons
  SetOutPath $INSTDIR
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
  WriteIniStr "$INSTDIR\${PRODUCT_NAME}.url" "InternetShortcut" "URL" "${PRODUCT_WEB_SITE}"
  CreateDirectory "$SMPROGRAMS\$ICONS_GROUP"
  CreateShortCut "$DESKTOP\BangolfResultat.lnk" "$INSTDIR\BangolfResultat.jar" "" "$INSTDIR\data\ikoner.icl" 0 SW_SHOWNORMAL
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\BangolfResultat.lnk" "$INSTDIR\BangolfResultat.jar" "" "$INSTDIR\data\ikoner.icl" 0 SW_SHOWNORMAL
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\Manual.lnk" "$INSTDIR\doc\manual.htm"
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\Hemsida.lnk" "$INSTDIR\${PRODUCT_NAME}.url"
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\Uninstall.lnk" "$INSTDIR\uninst.exe"
  !insertmacro MUI_STARTMENU_WRITE_END
SectionEnd

Section -Post
  WriteUninstaller "$INSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayName" "$(^Name)"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "UninstallString" "$INSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayVersion" "${PRODUCT_VERSION}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "URLInfoAbout" "${PRODUCT_WEB_SITE}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "Publisher" "${PRODUCT_PUBLISHER}"
SectionEnd


Function un.onUninstSuccess
  HideWindow
  MessageBox MB_ICONINFORMATION|MB_OK "$(^Name) �r nu borttagit fr�n din dator."
FunctionEnd

Function un.onInit
  MessageBox MB_ICONQUESTION|MB_YESNO|MB_DEFBUTTON2 "�r du s�ker p� att du vill avinstallera $(^Name) och alla dess komponenter?" IDYES +2
  Abort
FunctionEnd

Section Uninstall
  !insertmacro MUI_STARTMENU_GETFOLDER "Application" $ICONS_GROUP
  Delete "$INSTDIR\${PRODUCT_NAME}.url"
  Delete "$INSTDIR\uninst.exe"
  Delete "$INSTDIR\data\snittstring"
  Delete "$INSTDIR\data\snittmap"
  Delete "$INSTDIR\data\snittapp"
  Delete "$INSTDIR\data\snitt"
  Delete "$INSTDIR\data\ptrack"
  Delete "$INSTDIR\data\pnametrack"
  Delete "$INSTDIR\data\orientation"
  Delete "$INSTDIR\data\namn"
  Delete "$INSTDIR\data\licensenamemap"
  Delete "$INSTDIR\data\licensemap"
  Delete "$INSTDIR\data\klasstring"
  Delete "$INSTDIR\data\klassmap"
  Delete "$INSTDIR\data\klass"
  Delete "$INSTDIR\data\ikoner.icl"
  Delete "$INSTDIR\data\dirsnitt"
  Delete "$INSTDIR\data\dirskv"
  Delete "$INSTDIR\data\dirjmf"
  Delete "$INSTDIR\data\dirhtm"
  Delete "$INSTDIR\data\directory"
  Delete "$INSTDIR\data\comparefiles"
  Delete "$INSTDIR\data\compareby"
  Delete "$INSTDIR\data\compare"
  Delete "$INSTDIR\data\classorder"
  Delete "$INSTDIR\data\brikon.gif"
  Delete "$INSTDIR\doc\webbsida.htm"
  Delete "$INSTDIR\doc\versionhistory.htm"
  Delete "$INSTDIR\doc\system.htm"
  Delete "$INSTDIR\doc\sok.htm"
  Delete "$INSTDIR\doc\snittlista.htm"
  Delete "$INSTDIR\doc\snitt.htm"
  Delete "$INSTDIR\doc\om.htm"
  Delete "$INSTDIR\doc\manual.htm"
  Delete "$INSTDIR\doc\comp.htm"
  Delete "$INSTDIR\doc\backup.htm"
  Delete "$INSTDIR\doc\bilder\utseende.gif"
  Delete "$INSTDIR\doc\bilder\status.gif"
  Delete "$INSTDIR\doc\bilder\sort.gif"
  Delete "$INSTDIR\doc\bilder\sokning.gif"
  Delete "$INSTDIR\doc\bilder\snitt.gif"
  Delete "$INSTDIR\doc\bilder\save.gif"
  Delete "$INSTDIR\doc\bilder\rubrik.gif"
  Delete "$INSTDIR\doc\bilder\resultat.gif"
  Delete "$INSTDIR\doc\bilder\resinmat.gif"
  Delete "$INSTDIR\doc\bilder\redig.gif"
  Delete "$INSTDIR\doc\bilder\nyckel.gif"
  Delete "$INSTDIR\doc\bilder\klasstar.gif"
  Delete "$INSTDIR\doc\bilder\klasser.gif"
  Delete "$INSTDIR\doc\bilder\jmfsurface.gif"
  Delete "$INSTDIR\doc\bilder\jmfres.gif"
  Delete "$INSTDIR\doc\bilder\jmf.gif"
  Delete "$INSTDIR\doc\bilder\indata.gif"
  Delete "$INSTDIR\doc\bilder\bytanamn.gif"
  Delete "$INSTDIR\doc\bilder\bgr.gif"
  Delete "$INSTDIR\doc\bilder\align.gif"
  Delete "$INSTDIR\error.log"
  Delete "$INSTDIR\licens.txt"
  Delete "$INSTDIR\BangolfResultat.jar"

  Delete "$DESKTOP\BangolfResultat.lnk"
  Delete "$SMPROGRAMS\$ICONS_GROUP\BangolfResultat.lnk"
  Delete "$SMPROGRAMS\$ICONS_GROUP\Manual.lnk"
  Delete "$SMPROGRAMS\$ICONS_GROUP\Hemsida.lnk"
  Delete "$SMPROGRAMS\$ICONS_GROUP\Uninstall.lnk"
  Delete "$ICONS_GROUP.lnk"

  RMDir "$SMPROGRAMS\$ICONS_GROUP"
  RMDir "$INSTDIR\doc\bilder"
  RMDir "$INSTDIR\doc"
  RMDir "$INSTDIR\data"
  RMDir /REBOOTOK "$INSTDIR"

  DeleteRegKey ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}"
  SetAutoClose true
SectionEnd