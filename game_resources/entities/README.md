# Entities
These image are used for the player, enemies, friendlies and items. Each entity has its own folder. Many of the images have multiple subimages which are determined by the grid. You can use the templates to easily tell the subimages apart.

---
### Friendlies
Each friendly get their own numbered folder. In it, you can change what they look like and what they say. If you removed the speech.txt the friendly will not say anything. You can also create more friendlies this way. To see how to do that, check out the zones README.

---
### Grid
The entity images use a grid (just like the zone images). Some subimages may cover more than one grid square. The only way of knowing if it does is by looking at the templates. The game only looks at the grid squares that the template subimages are inside of. If you draw outside those grid squares, the game will not see it.

---
### Static Images
The static poses for each entity are kept in one file called main.png. For example, in the player main.png, there are four standing positions, attacking pose, and death pose. Simply edit those individual subimages to change the entity.

---
### Animation
There are two ways of doing animation: GIFs or PNGs. GIFs might be easier to work with but don't support *translucency* (they do support *transparency*). Just keep this in mind when choosing which you want to use.

##### PNG
All the template images use PNGs so that you can see how they work. Simply put all the frames of your animation on top of eachother.

**Extending or Shortening Animations**  
The grid height and width **must** be the same for each individual frame **but** you can extend/shorten the animation by extending/shortening the height of the image by intervals of the entity frame height (add/remove whole frames the animation uses). For example, if you wanted to extend the player attack animations, increase the height of the image by 96px to add one frame because one frame is 3 grid tiles high.

##### GIF
Gifs must have the same width as the animation PNG equivalent and have the same height as a single frame in the PNG equaivalent (both will be a multiple of 32px). The GIF can be as long as you want. To get the game to use a GIF instead of the PNG simply rename the new GIF to be the same as the old PNG. If the game finds both a PNG and a GIF with the same name, it will choose the PNG. You must change the name of the old PNG to something else such as walkW_old.png.