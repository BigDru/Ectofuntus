Ectofuntus
==========

Request URL: http://www.powerbot.org/community/topic/1229458-osrs-ectofungus/



Requires:

1) Completion of Ghosts Ahoy

2) Ectophial

3) 9 buckets

4) 9 pots

5) lots of bones (script does not stop when you run out of bones. It also glitches when you withdraw less than 9.)

6) Ghostspeak amulet equipped.



Optional: 

If you have 58+ agility, shortcut will be used to get to the Pool of Slime. (Has not been tested as I don't have an account with a sufficiently high agility level.


Notes:

- This script currently only works with regular "Bones". (v1.04 will include possibility to use other bones).
- I have tested with 1k bones. Some minor glitches occurred every now and then but it runs pretty smoothly. 
- I cannot test the use of the shortcut, someone please PM me on powerbot.org and let me know if it works or not.
- I would advice you monitor as the script runs. My script does not handle randoms nor will it ever. However, I made the script sufficiently modular that you should be able to pause and deal with the random. If you receive an item from the random and you have space for it in inventory, it will be automatically deposited in the bank. 
- Theoretically, you can start from anywhere in RS2007 as long as you have a full ectophial. If you have an empty ectophial and you are at the ectofuntus, the script will automatically fill it up. 
- Note that you may need to pause the script and put in your bank pin if you have one. After you've done this, close the bank and resume the script. The client should be able to handle this but I have not been able to get it to work in my testing. 
- Script will glitch if it fails to retrieve Ecto-tokens.
- Script does handle running to reduce time/run.


Version Info:

v1.03
- Added separate UI thread
- Added varpbit implementation to split Grind.java into 3 tasks. If bones are accidentally buried, script can recover. 
- Implemented use of Condition class. 
- Added singleton setting structure to store global script information. 

v1.02
 A lot of changes have been made here. 
- The previous tasks were broken down quite a bit to allow the script to autocorrect more easily. 
- Areas have also been incorporated in this version. 
- Exhaustive testing (600 bones)has been done to ensure that the script works well. 
- Antiban reaction buffers have been added to each task. (Script will wait a random short period of time before continuing).



v1.01
 - Basic functions work
