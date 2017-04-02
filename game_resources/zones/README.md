# Zones

Zones consist of 3 images:
 - the top image          (top.png)
 - and the bottom image   (bottom.png)
 - the layout image       (layout.png)

If you do not care for changing the location of the walls or entities skip the layout image section. The tiles folder are for dynamic tiles that cannot go on the bottom/top image.

--------------------------------
### Top and Bottom Image
 
These are the images that are actually displayed to the player. In the photoshop document you will see a grid layer (just like the one for entities). Each grid square in this image represents the same thing a pixel does in the layout image. Use this grid to help align you image with the walls the game uses.

The bottom image (zoneb.png) is displayed beneath the player, enemies and friendlies while the top (zonet.png) is displayed on top. This way you can create things such as trees or buildings that the player can go behind. Too complicated or difficult to manage? Okay, just make the top image 100% transparent and use the bottom image. You just won't be able to go behind anything.

--------------------------------
### Layout Image

In the layout image, each pixel represents one square (32px X 32px) in the actual game and reads the RGB/hexadecimal value. The program can read up to 16,581,375 color codes! Unfortunately for us, most of those look the same. These colors can look similar but as long as you get the codes right, you'll be okay.

##### Zone size
You can change the size of the image as well to make the zone bigger/smaller. Know that for every pixel in the layout image equals 32 pixels in the top and bottom image. This means if you add 2 pixels to the height and 3 pixels to the width of the layout image, you must add 64 (2x32) pixels to the height of the bottom and top image and 96 (3x32) pixels to the width.

##### Additional zones
If you want to add more zones to extend the game, you can. Just create more zone folders. So, instead of stopping at zone3, create a folder called zone4 and make sure it has the zonew.png, zonet.png and zoneb.png images. 

##### !!!!! IMPORTANT !!!!!
When saving this image go to 'Save As' and change the file type to PNG. Do **not** go to 'Save for Web' and save as a PNG there.

--------
### Pixel Color Charts
##### Tiles
|     Tile         |        RGB        | Hexadecimal     |
|:----------------:|:-----------------:|:---------------:|
| Open Space       | (x,x,x)           | #xxxxxx         |
| Wall             | (255,255,255)     | #FFFFFF         |
| Pit              | (255,0,255)       | #FF00FF         |
| **Player Spawn** | (0,0-254,255)     | #0000FF         |
| **Teleport**     | (255,255,0-254)   | #FFFF00-#FFFFFE |
| **Door**         | (51-54,51-53,150) | #333396-#363596 |
| **Destrucatble** | (1-3,150,0)       | #019600-#039600 |

**Player Spawn**
Where the player will spawn if arriving from the zone with the same number as the green value. For example, if you are teleporting from zone 3 to zone 5, the player will spawn at the location of the pixel with the value (0,3,255). If that pixel cannot be found or the player is being spawned in for the first time, the player will spawn at the pixel with the value (0,0,255).

**Teleport**  
The area that moves the player to the next zone. The blue value changes which zone the player teleports to. If 1, the player will teleport to zone1. If 0, the player will teleport to the next zone.

**Door**  
A door that needs to be opened by the correct key. The red value determines the direction of the door (This only affects how the door appears not any mechanics). The green value determind the type of key that is needed to open it.

| Value | Direction (red) | Key Required (green) |
|:-----:|:---------------:|:--------------------:|
| 51    | NORTH           | Tier I               |
| 52    | EAST            | Tier II              |
| 53    | SOUTH           | Tier III             |
| 54    | WEST            |                      |

**Destructable**  
An object that has collision unless the player breaks it. When broken the object has a 33% chance of dropping money I and 33% chance of dropping money II. The red value changes which box is spawned.

| Red Value |    Image    |
|:---------:|:-----------:|
| 1         | object1.png |
| 2         | object2.png |
| 3         | object3.png |

##### Creatures
|   Creature   |       RGB       |   Hexadecimal  |
|:------------:|:---------------:|:--------------:|
| Basic Enemy  | (255,0,0)       | #FF0000        |
| **Boss**     | (0-1,255,255)   | #00FFFF        |
| **Friendly** | (0-254,255,0-3) | #00FF00-FEFF00 |

**Boss**
There are two types of bosses: mini and final. Mini bosses are just tougher opponents that use 0 for the red value. Final bosses will end the game if they are killed in the final zone. They use 1 for the red value.

**Friendly**  
Friendlies talk to the player when interacted with. The red value changes what folder the friendly will go to for its images and speech. For example, if the value is one, the friendly will use the files located in /game_resources/entities/friendly/1/. This way you can keep creating more friendlies. More details on how to customize the friendlies in the entites README.

The blue value changes the direction the friendly is initially facing. 0=North, 1=East, 2=South, 3=West

##### Items
Items always use 150 green value. The blue value is determined by the item ID. While the red value determines the cost. Cost = (red value - 100) * 5 *or* red value = cost / 5 + 100. For example, the RGB value for a free key I is: (100,150,1). While the RGB value for a sword II is that costs twenty is: (104,150,8). Also, money I gives 1 money, money II give 5 money and money III gives 10 money.

|    Item    | ID |       RGB       | Hexadecimal |
|:----------:|:--:|:---------------:|:-----------:|
| Money I    | 1  | (100,150,1)     | #404001     |
| Money II   | 2  | (100,150,2)     | #404002     |
| Money III  | 3  | (100,150,3)     | #404003     |
| Key I      | 4  | (100-254,150,4) | #404004     |
| Key II     | 5  | (100-254,150,5) | #404005     |
| Key III    | 6  | (100-254,150,6) | #404006     |
| Sword I    | 7  | (100-254,150,7) | #404007     |
| Sword II   | 8  | (100-254,150,8) | #404008     |
| Sword III  | 9  | (100-254,150,9) | #404009     |