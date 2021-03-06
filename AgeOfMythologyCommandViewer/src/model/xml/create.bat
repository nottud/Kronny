set projectPath=E:\Git\Repository\AgeOfMythologyCommandViewer
set jdkPath=C:\Program Files\Java\jdk1.8.0_131\bin
"%jdkPath%\xjc" -extension -p model.xml -d "%projectPath%\src" "%projectPath%\src\model\xml\schema.xsd"
PAUSE