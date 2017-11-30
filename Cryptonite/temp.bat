attrib +s "C:\Users\JYB\Desktop\보호폴더"
echo [.ShellClassInfo] >>C:\Users\JYB\Desktop\보호폴더\desktop.ini
echo ConfirmFileOp=0 >>C:\Users\JYB\Desktop\보호폴더\desktop.ini
echo NoSharing=1 >>C:\Users\JYB\Desktop\보호폴더\desktop.ini
echo IconFile=C:\Users\JYB\git\Cryptonite\Cryptonite\_folder.ico >>C:\Users\JYB\Desktop\보호폴더\desktop.ini
echo IconIndex=0 >>C:\Users\JYB\Desktop\보호폴더\desktop.ini
echo InfoTip=Cryptonite >>C:\Users\JYB\Desktop\보호폴더\desktop.ini
attrib +S +H C:\Users\JYB\Desktop\보호폴더\desktop.ini
taskkill /f /im explorer.exe
explorer