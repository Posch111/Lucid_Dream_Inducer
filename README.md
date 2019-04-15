# REM Sleep Cycle Monitor
EOG Sleep cycle monitor project
---
Installing Kicad and set up libraries
---
Windows/Mac
* Download KiCAD from [the KiCAD website](http://kicad-pcb.org/download/) to download KiCAD 5+ and 6+ when 6 is released.
* Follow installation wizards, and install KiCAD.
* Through the terminal, clone the repository into a git folder with the 'git clone <URL>' command when you are in the desired folder.
**Mac user NOTE:** if you get this error 
  
  `xcrun: error: invalid active developer path
 (/Library/Developer/CommandLineTools), missing xcrun at:
 /Library/Developer/CommandLineTools/usr/bin/xcrun`
 use this command in the terminal: `xcode-select --install`
* In KiCAD, go to File > Open Project and navigate to the /<git directory>/REM_Sleep_Cycle_Monitor/Hardware/KICAD/REM_sleep_analysis.pro to click "Open"
* Open the schematic editor and click the second option which says "Import from file" with the file "sim-lib-table" in the same folder as the project file.
* In the schematic editor, go to Preferences > Manage Symbol Libraries...
* Change to the "Project Specific Libraries" tab
  
  
Ubuntu
* Add ppa through terminal with these commands:

       sudo add-apt-repository --yes ppa:js-reynaud/kicad-5
       sudo apt update
       sudo apt install kicad

* Through the terminal, clone the repository into a git folder with the 'git clone <URL>' command when you are in the desired folder.
* In KiCAD, go to File > Open Project and navigate to the /<git directory>/REM_Sleep_Cycle_Monitor/Hardware/KICAD/REM_sleep_analysis.pro to click "Open"
  
Arch Linux

      sudo pacman -S kicad 

* Through the terminal, clone the repository into a git folder with the 'git clone <URL>' command when you are in the desired folder.
* In KiCAD, go to File > Open Project and navigate to the /<git directory>/REM_Sleep_Cycle_Monitor/Hardware/KICAD/REM_sleep_analysis.pro to click "Open"

---
Installing Android Studio
---
https://developer.android.com/studio/

---
Soldering and Testing Device
---
TODO

---
Bluetooth Operations
---
TODO

----
Bluetooth Command List
----
TODO

<!-- Target:  GitHub Flavor Markdown.  To test locally:  pandoc -f markdown_github -t html README.md  -->
