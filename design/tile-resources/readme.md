Creation of tiles of floor 4:
Needs to be executed in bash and not zsh

convert exactum4.png -crop 256x256 -set filename:tile %[fx:page.x/256]_%[fx:page.y/256] +repage +adjoin export/tile-4-%[filename:tile].png
