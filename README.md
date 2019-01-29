# REM Sleep Cycle Monitor
EOG Sleep cycle monitor project


---
Introduction
---
TODO

---
Installing Kicad and set up libraries
---
TODO
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

TODO
**NOTE:** 

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

| Compat config string  | Environment Variable           | Description  |
| :-------------------- | :----------------------------- | :----------- |
|                       | <tt>PROTON_LOG</tt>            | Convenience method for dumping a useful debug log to `$HOME/steam-$APPID.log`. For more thorough logging, use `user_settings.py`. |
|                       | <tt>PROTON_DUMP_DEBUG_COMMANDS</tt> | When running a game, Proton will write some useful debug scripts for that game into `$PROTON_DEBUG_DIR/proton_$USER/`. |
|                       | <tt>PROTON_DEBUG_DIR<tt>       | Root directory for the Proton debug scripts, `/tmp` by default. |
| <tt>wined3d</tt>      | <tt>PROTON_USE_WINED3D</tt>    | Use OpenGL-based wined3d instead of Vulkan-based DXVK for d3d11 and d3d10. This used to be called `PROTON_USE_WINED3D11`, which is now an alias for this same option. |
| <tt>nod3d11</tt>      | <tt>PROTON_NO_D3D11</tt>       | Disable <tt>d3d11.dll</tt>, for games which can fall back to and run better with d3d9. |
| <tt>noesync</tt>      | <tt>PROTON_NO_ESYNC</tt>       | Do not use eventfd-based in-process synchronization primitives. |

<!-- Target:  GitHub Flavor Markdown.  To test locally:  pandoc -f markdown_github -t html README.md  -->
