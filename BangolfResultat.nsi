; Script generated by the HM NIS Edit Script Wizard.

; HM NIS Edit Wizard helper defines
!define PRODUCT_NAME "BangolfResultat"
!define PRODUCT_VERSION "0.7 beta"
!define PRODUCT_PUBLISHER "Magnus Nilsson"
!define PRODUCT_WEB_SITE "http://bangolfresultat.webhop.org/"
!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"
!define PRODUCT_UNINST_ROOT_KEY "HKLM"
!define PRODUCT_STARTMENU_REGVAL "NSIS:StartMenuDir"
!define PRODUCT_WORKSPACE "E:\eclipse\workspace"
;!define PRODUCT_WORKSPACE "C:\eclipse\workspace"

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
!insertmacro MUI_PAGE_LICENSE "${PRODUCT_WORKSPACE}\BangolfResultat\doc\licens.txt"
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
  SetOverwrite on
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\licens.txt"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\BangolfResultat.jar"
  SetOutPath "$INSTDIR\doc\bilder"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\bilder\align.gif"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\bilder\bgr.gif"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\bilder\bytanamn.gif"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\bilder\indata.gif"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\bilder\jmf.gif"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\bilder\jmfres.gif"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\bilder\jmfsurface.gif"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\bilder\klasser.gif"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\bilder\klasstar.gif"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\bilder\redig.gif"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\bilder\resinmat.gif"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\bilder\resultat.gif"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\bilder\rubrik.gif"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\bilder\save.gif"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\bilder\snitt.gif"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\bilder\sokning.gif"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\bilder\sort.gif"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\bilder\status.gif"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\bilder\utseende.gif"
  SetOutPath "$INSTDIR\doc"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\backup.htm"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\comp.htm"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\manual.htm"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\om.htm"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\snitt.htm"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\snittlista.htm"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\sok.htm"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\system.htm"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\versionhistory.htm"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\doc\webbsida.htm"
  SetOutPath "$INSTDIR\data"
  SetOverwrite off
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\brikon.gif"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\classorder"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\compare"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\compareby"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\comparefiles"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\directory"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\dirhtm"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\dirjmf"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\dirskv"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\dirsnitt"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\ikoner.icl"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\klass"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\klassmap"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\klasstring"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\licensemap"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\licensenamemap"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\namn"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\orientation"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\pnametrack"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\ptrack"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\snitt"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\snittapp"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\snittitle"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\snittmap"
  File "${PRODUCT_WORKSPACE}\BangolfResultat\installer\data\snittstring"
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
  Delete "$INSTDIR\data\snittitle"
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