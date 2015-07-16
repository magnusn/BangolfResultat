; Script generated by the HM NIS Edit Script Wizard.

; Defines variables
Var /GLOBAL previous_uninstaller
Var /GLOBAL previous_uninstaller_path
Var /GLOBAL previous_installed_version
Var /GLOBAL previous_compared_to_0_8
Var /GLOBAL remove_settings

; HM NIS Edit Wizard helper defines
!define PRODUCT_NAME "BangolfResultat"
!define PRODUCT_VERSION "0.8pre1"
!define PRODUCT_PUBLISHER "Magnus Hov�n"
!define PRODUCT_WEB_SITE "http://bangolfresultat.manet.se/"
!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"
!define PRODUCT_UNINST_ROOT_KEY "HKLM"
!define PRODUCT_INSTALL_KEY "Software\${PRODUCT_NAME}"
!define PRODUCT_INSTALL_ROOT_KEY "HKLM"
!define PRODUCT_STARTMENU_REGVAL "NSIS:StartMenuDir"
!define PRODUCT_PROJECT_PATH "."
!define PRODUCT_APPDATA_DIRECTORY "$APPDATA\${PRODUCT_NAME}"
!define PRODUCT_SETTINGS_DIRECTORY "${PRODUCT_APPDATA_DIRECTORY}\Settings"

; Include file functions
!include "FileFunc.nsh"

; Include word functions
!include "WordFunc.nsh"

; MUI 1.67 compatible ------
!include "MUI.nsh"

; MUI Settings
!define MUI_ABORTWARNING
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install-blue.ico"
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall-blue.ico"

; Welcome page
!define MUI_WELCOMEPAGE_TEXT "Om du redan har en version av ${PRODUCT_NAME} installerad skall du avsluta programmet och s�kerhetskopiera programmets inst�llningar (se manualen) innan du forts�tter med installationen.\r\n\r\n$_CLICK"
!insertmacro MUI_PAGE_WELCOME
; License page
!insertmacro MUI_PAGE_LICENSE "${PRODUCT_PROJECT_PATH}\doc\licens.txt"
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
!define MUI_FINISHPAGE_SHOWREADME_NOTCHECKED
!insertmacro MUI_PAGE_FINISH

; Uninstaller pages
!insertmacro MUI_UNPAGE_INSTFILES

; Language files
!insertmacro MUI_LANGUAGE "Swedish"

; MUI end ------

Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
OutFile "installer\${PRODUCT_NAME} ${PRODUCT_VERSION}.exe"
InstallDir "$PROGRAMFILES\${PRODUCT_NAME}"
InstallDirRegKey ${PRODUCT_INSTALL_ROOT_KEY} "${PRODUCT_INSTALL_KEY}" "InstallDir"
RequestExecutionLevel admin
ShowInstDetails show
ShowUnInstDetails show

; The "" makes the section hidden.
Section "" UninstallPrevious
  Call UninstallPrevious
SectionEnd

Function UninstallPrevious
  ; Read register entries
  ReadRegStr $previous_uninstaller ${PRODUCT_UNINST_ROOT_KEY} ${PRODUCT_UNINST_KEY} "UninstallString"
  ReadRegStr $previous_installed_version ${PRODUCT_UNINST_ROOT_KEY} ${PRODUCT_UNINST_KEY} "DisplayVersion"

  ; Exit function if no register entry
  ${If} $previous_uninstaller == ""
    Goto Done
  ${EndIf}

  ; Exit function if uninstaller file does not exist
  IfFileExists "$previous_uninstaller" 0 Done

  ; Check previous version number. If older than 0.8: data folder should be moved and uninstaller can't be run silently.
  ${VersionCompare} "$previous_installed_version" "0.8" $previous_compared_to_0_8

  DetailPrint "Avinstallerar f�reg�ende version"

  ; Run the uninstaller
  ${If} $previous_compared_to_0_8 == 2
    MessageBox MB_ICONINFORMATION|MB_OK "F�ljande migreringssteg kommer nu att utf�ras:$\r$\n$\r$\n* Programmets inst�llningar kommer att flyttas fr�n $\"$INSTDIR\data$\" till $\"${PRODUCT_SETTINGS_DIRECTORY}$\"$\r$\n* F�reg�ende version kommer att avinstalleras" /SD IDOK
    Call MoveDataFolder
  ${EndIf}
  Call RunUninstaller
  Done:
FunctionEnd

Function RunUninstaller
  ${GetParent} "$previous_uninstaller" $previous_uninstaller_path
  Uninstall:
    ClearErrors
    ${If} $previous_compared_to_0_8 == 2
      ExecWait '"$previous_uninstaller" _?=$previous_uninstaller_path'
    ${Else}
      ExecWait '"$previous_uninstaller" /S _?=$previous_uninstaller_path'
    ${EndIf}
    IfErrors AskForRetry Done
    AskForRetry:
      MessageBox MB_ICONEXCLAMATION|MB_ABORTRETRYIGNORE|MB_DEFBUTTON2 "Misslyckades att avinstallera f�reg�ende version.$\r$\n$\r$\nKontrollera att ${PRODUCT_NAME} inte k�r och f�rs�k sedan igen." /SD IDIGNORE IDRETRY Uninstall IDIGNORE Done
      Abort "Misslyckades att avinstallera f�reg�ende version"
  Done:
FunctionEnd

Function MoveDataFolder
  IfFileExists "$INSTDIR\data" MoveDataFolder Done
  MoveDataFolder:
    ClearErrors
    CopyFiles "$INSTDIR\data\*.*" "${PRODUCT_SETTINGS_DIRECTORY}\"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\ikoner.icl"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\brikon.gif"
    IfErrors AskForRetry RemoveSource
    AskForRetry:
      MessageBox MB_ICONEXCLAMATION|MB_ABORTRETRYIGNORE|MB_DEFBUTTON2 "Misslyckades att kopiera filerna fr�n $\"$INSTDIR\data$\" till $\"${PRODUCT_SETTINGS_DIRECTORY}$\".$\r$\n$\r$\nSe till s� att filerna inte anv�nds och f�rs�k sedan igen." /SD IDIGNORE IDRETRY MoveDataFolder IDIGNORE Done
      Abort "Misslyckades att kopiera filerna fr�n $\"$INSTDIR\data$\" till $\"${PRODUCT_SETTINGS_DIRECTORY}$\"."
    RemoveSource:
      RMDir /r "$INSTDIR\data"
  Done:
FunctionEnd

Section "MainSection" SEC01
  SetOutPath "$INSTDIR"
  SetOverwrite on
  File "${PRODUCT_PROJECT_PATH}\doc\licens.txt"
  File "${PRODUCT_PROJECT_PATH}\installer\BangolfResultat.jar"
  SetOutPath "$INSTDIR\doc\bilder"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\align.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\bgr.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\bytanamn.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\indata.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\jmf.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\jmfres.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\jmfsurface.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\klasser.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\klasstar.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\lafjava.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\lafsystem.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\redig.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\resinmat.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\resultat.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\rubrik.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\save.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\snitt.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\snittclub.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\sokning.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\sort.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\status.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\tabtitle.gif"
  File "${PRODUCT_PROJECT_PATH}\doc\bilder\utseende.gif"
  SetOutPath "$INSTDIR\doc"
  File "${PRODUCT_PROJECT_PATH}\doc\backup.htm"
  File "${PRODUCT_PROJECT_PATH}\doc\comp.htm"
  File "${PRODUCT_PROJECT_PATH}\doc\lookandfeel.htm"
  File "${PRODUCT_PROJECT_PATH}\doc\knownissues.htm"
  File "${PRODUCT_PROJECT_PATH}\doc\manual.htm"
  File "${PRODUCT_PROJECT_PATH}\doc\om.htm"
  File "${PRODUCT_PROJECT_PATH}\doc\snitt.htm"
  File "${PRODUCT_PROJECT_PATH}\doc\snittlista.htm"
  File "${PRODUCT_PROJECT_PATH}\doc\sok.htm"
  File "${PRODUCT_PROJECT_PATH}\doc\system.htm"
  File "${PRODUCT_PROJECT_PATH}\doc\versionhistory.htm"
  File "${PRODUCT_PROJECT_PATH}\doc\webbsida.htm"
  SetOutPath "$INSTDIR\icons"
  File "${PRODUCT_PROJECT_PATH}\icons\brikon.gif"
  File "${PRODUCT_PROJECT_PATH}\icons\ikoner.icl"
  SetOutPath "${PRODUCT_SETTINGS_DIRECTORY}"
  SetOverwrite off
  File "${PRODUCT_PROJECT_PATH}\installer\settings\classorder"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\compare"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\compareby"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\comparefiles"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\datastore"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\directory"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\dirhtm"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\dirjmf"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\dirskv"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\dirsnitt"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\klass"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\klassmap"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\klasstring"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\licensemap"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\licensenamemap"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\namn"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\orientation"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\pnametrack"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\ptrack"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\snitt"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\snittapp"
  File "${PRODUCT_PROJECT_PATH}\installer\settings\snittstring"
SectionEnd

Section -AdditionalIcons
  SetOutPath $INSTDIR
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
  WriteIniStr "$INSTDIR\${PRODUCT_NAME}.url" "InternetShortcut" "URL" "${PRODUCT_WEB_SITE}"
  CreateDirectory "$SMPROGRAMS\$ICONS_GROUP"
  CreateShortCut "$DESKTOP\BangolfResultat.lnk" "$INSTDIR\BangolfResultat.jar" "" "$INSTDIR\icons\ikoner.icl" 0 SW_SHOWNORMAL
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\BangolfResultat.lnk" "javaw.exe" '-jar "$INSTDIR\BangolfResultat.jar"' "$INSTDIR\icons\ikoner.icl" 0 SW_SHOWNORMAL
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
  WriteRegStr ${PRODUCT_INSTALL_ROOT_KEY} "${PRODUCT_INSTALL_KEY}" "InstallDir" "$INSTDIR"
SectionEnd


Function un.onUninstSuccess
  HideWindow
  MessageBox MB_ICONINFORMATION|MB_OK "$(^Name) �r nu borttagit fr�n din dator." /SD IDOK
FunctionEnd

Function un.onInit
  MessageBox MB_ICONQUESTION|MB_YESNOCANCEL|MB_DEFBUTTON3 "$(^Name) kommer att avinstalleras.$\r$\n$\r$\nVill du �ven ta bort programmets inst�llningar?" /SD IDNO IDYES RemoveSettings IDNO Done
  Abort
  RemoveSettings:
    StrCpy $remove_settings "yes"
  Done:
FunctionEnd

Section Uninstall
  !insertmacro MUI_STARTMENU_GETFOLDER "Application" $ICONS_GROUP
  Delete "$INSTDIR\${PRODUCT_NAME}.url"
  Delete "$INSTDIR\uninst.exe"
  ${If} $remove_settings == "yes"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\snittstring"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\snittapp"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\snitt"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\ptrack"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\pnametrack"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\orientation"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\namn"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\licensenamemap"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\licensemap"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\klasstring"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\klassmap"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\klass"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\dirsnitt"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\dirskv"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\dirjmf"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\dirhtm"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\directory"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\datastore"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\comparefiles"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\compareby"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\compare"
    Delete "${PRODUCT_SETTINGS_DIRECTORY}\classorder"
  ${EndIf}
  Delete "$INSTDIR\icons\ikoner.icl"
  Delete "$INSTDIR\icons\brikon.gif"
  Delete "$INSTDIR\doc\webbsida.htm"
  Delete "$INSTDIR\doc\versionhistory.htm"
  Delete "$INSTDIR\doc\system.htm"
  Delete "$INSTDIR\doc\sok.htm"
  Delete "$INSTDIR\doc\snittlista.htm"
  Delete "$INSTDIR\doc\snitt.htm"
  Delete "$INSTDIR\doc\om.htm"
  Delete "$INSTDIR\doc\manual.htm"
  Delete "$INSTDIR\doc\knownissues.htm"
  Delete "$INSTDIR\doc\lookandfeel.htm"
  Delete "$INSTDIR\doc\comp.htm"
  Delete "$INSTDIR\doc\backup.htm"
  Delete "$INSTDIR\doc\bilder\utseende.gif"
  Delete "$INSTDIR\doc\bilder\tabtitle.gif"
  Delete "$INSTDIR\doc\bilder\status.gif"
  Delete "$INSTDIR\doc\bilder\sort.gif"
  Delete "$INSTDIR\doc\bilder\sokning.gif"
  Delete "$INSTDIR\doc\bilder\snittclub.gif"
  Delete "$INSTDIR\doc\bilder\snitt.gif"
  Delete "$INSTDIR\doc\bilder\save.gif"
  Delete "$INSTDIR\doc\bilder\rubrik.gif"
  Delete "$INSTDIR\doc\bilder\resultat.gif"
  Delete "$INSTDIR\doc\bilder\resinmat.gif"
  Delete "$INSTDIR\doc\bilder\redig.gif"
  Delete "$INSTDIR\doc\bilder\nyckel.gif"
  Delete "$INSTDIR\doc\bilder\lafsystem.gif"
  Delete "$INSTDIR\doc\bilder\lafjava.gif"
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
  RMDir "$INSTDIR\icons"
  RMDir "${PRODUCT_SETTINGS_DIRECTORY}"
  RMDir "${PRODUCT_APPDATA_DIRECTORY}"
  RMDir /REBOOTOK "$INSTDIR"

  DeleteRegKey ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}"
  DeleteRegKey ${PRODUCT_INSTALL_ROOT_KEY} "${PRODUCT_INSTALL_KEY}"
  SetAutoClose false
SectionEnd