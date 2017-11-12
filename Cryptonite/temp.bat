attrib +s "C:\Users\±ËπŒ¡§\Desktop\test2"
echo [.ShellClassInfo] >>C:\Users\±ËπŒ¡§\Desktop\test2\desktop.ini
echo ConfirmFileOp=0 >>C:\Users\±ËπŒ¡§\Desktop\test2\desktop.ini
echo NoSharing=1 >>C:\Users\±ËπŒ¡§\Desktop\test2\desktop.ini
echo IconFile=C:\Users\±ËπŒ¡§\Desktop\Cryptonite-master\Cryptonite\_folder.ico >>C:\Users\±ËπŒ¡§\Desktop\test2\desktop.ini
echo IconIndex=0 >>C:\Users\±ËπŒ¡§\Desktop\test2\desktop.ini
echo InfoTip=Cryptonite >>C:\Users\±ËπŒ¡§\Desktop\test2\desktop.ini
attrib +S +H C:\Users\±ËπŒ¡§\Desktop\test2\desktop.ini
taskkill /f /im explorer.exe
explorer