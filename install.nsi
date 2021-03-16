; The name of the installer
Name "Pando Print Bridge App"

; The file to write
OutFile "Pando-Print.exe"

; The default installation directory
InstallDir "$LOCALAPPDATA\Pando Print Bridge App"

; Request application privileges for Windows Vista
RequestExecutionLevel user

;--------------------------------

; Pages

;Page directory
Page components
Page instfiles

;--------------------------------

; The stuff to install
Section "!Main Application" ;No components page, name is not important
  SectionIn RO

  ; Set output path to the installation directory.
  SetOutPath $INSTDIR
  
  ; Put file there
  File /r out\artifacts\webapp_hardware_bridge_jar\*
  File /r jre
  File /r cert
  
  File "install.nsi"
  
  ; Delete shortcuts
  Delete "$DESKTOP\Pando Print Bridge App.lnk"
  Delete "$DESKTOP\Pando Print Bridge App (GUI).lnk"
  Delete "$DESKTOP\Pando Print Bridge App (Configurator).lnk"
  Delete "$SMPROGRAMS\Pando Print Bridge App.lnk"
  Delete "$SMPROGRAMS\Pando Print Bridge App (GUI).lnk"
  Delete "$SMPROGRAMS\Pando Print Bridge App (Configurator).lnk"
  
  ; Create shortcuts
  CreateShortcut "$DESKTOP\Pando Print Bridge App.lnk" "$INSTDIR\jre\bin\javaw.exe" "-cp webapp-hardware-bridge.jar tigerworkshop.webapphardwarebridge.GUI"
  #CreateShortcut "$DESKTOP\Pando Print Bridge App (CLI).lnk" "$INSTDIR\jre\bin\java.exe" "-cp webapp-hardware-bridge.jar tigerworkshop.webapphardwarebridge.Server"
  #CreateShortcut "$DESKTOP\Pando Print Bridge App (Configurator).lnk" "$INSTDIR\jre\bin\javaw.exe" "-cp webapp-hardware-bridge.jar tigerworkshop.webapphardwarebridge.Configurator"
  
  CreateShortcut "$SMPROGRAMS\Pando Print Bridge App.lnk" "$INSTDIR\jre\bin\javaw.exe" "-cp webapp-hardware-bridge.jar tigerworkshop.webapphardwarebridge.GUI"
  CreateShortcut "$SMPROGRAMS\Pando Print Bridge App (CLI).lnk" "$INSTDIR\jre\bin\java.exe" "-cp webapp-hardware-bridge.jar tigerworkshop.webapphardwarebridge.Server"
  CreateShortcut "$SMPROGRAMS\Pando Print Bridge App (Configurator).lnk" "$INSTDIR\jre\bin\javaw.exe" "-cp webapp-hardware-bridge.jar tigerworkshop.webapphardwarebridge.Configurator"
  
  ; Write the installation path into the registry
  WriteRegStr HKCU "SOFTWARE\Pando Print Bridge App" "Install_Dir" "$INSTDIR"
  
  ; Write the uninstall keys for Windows
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\Pando Print Bridge App" "DisplayName" "Pando Print Bridge App"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\Pando Print Bridge App" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWORD HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\Pando Print Bridge App" "NoModify" 1
  WriteRegDWORD HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\Pando Print Bridge App" "NoRepair" 1
  WriteUninstaller "uninstall.exe"

  ; Auto close when finished
  SetAutoClose true
  
SectionEnd ; end the section

Section "Auto-start" autostart
  CreateShortcut "$SMSTARTUP\Pando Print Bridge App.lnk" "$INSTDIR\jre\bin\javaw.exe" "-cp webapp-hardware-bridge.jar tigerworkshop.webapphardwarebridge.GUI"
SectionEnd

Section "Uninstall"
  
  ; Remove registry keys
  DeleteRegKey HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\Pando Print Bridge App"
  DeleteRegKey HKCU "SOFTWARE\Pando Print Bridge App"
  
  ; Delete shortcuts
  Delete "$DESKTOP\Pando Print Bridge App.lnk"
  Delete "$DESKTOP\Pando Print Bridge App (GUI).lnk"
  Delete "$DESKTOP\Pando Print Bridge App (Configurator).lnk"
  Delete "$SMPROGRAMS\Pando Print Bridge App.lnk"
  Delete "$SMPROGRAMS\Pando Print Bridge App (GUI).lnk"
  Delete "$SMPROGRAMS\Pando Print Bridge App (Configurator).lnk"
  Delete "$SMSTARTUP\Pando Print Bridge App.lnk"
  
  ; Remove files and uninstaller
  RMDir /r $INSTDIR

SectionEnd

Function .onInstSuccess
  Exec "$INSTDIR\jre\bin\javaw.exe -cp webapp-hardware-bridge.jar tigerworkshop.webapphardwarebridge.GUI"
FunctionEnd