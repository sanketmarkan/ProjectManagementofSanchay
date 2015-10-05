import pytest 
import player
import pygame
import main
import constants
import princess
import fireball
import coin
import landforms
import donkey

@pytest.fixture()
def define_sprites():
	screen = pygame.display.set_mode((constants.SCREEN_WIDTH, constants.SCREEN_HEIGHT))
	knight = player.Player()	
	knight.princess = princess.Princess(constants.SCREEN_HEIGHT - 100,constants.FOUR_Y)
	return knight


class TestPlayerClass:

	# Tests wether the player is at correct starting position
	def test_start_pos(self, define_sprites):
		assert define_sprites.rect.bottom == constants.SCREEN_HEIGHT
		assert define_sprites.rect.left == 0

		# Tests wether the sprite has been correctly loaded and image been correctly blitted
	def test_sprite_validity(self, define_sprites):
		print 'Checking dimensions'
		assert define_sprites.rect.height == 70
		assert define_sprites.rect.width == 50
		print 'Checking that its life is optimal'
		assert define_sprites.life == constants.PLAYER_LIFE 


	# Test wether gravity is added and wether it is switched off when a player is standing on a platform
	def test_gravity_effect(self, define_sprites):
		print 'Initializing players position'
		define_sprites.rect.bottom == constants.SCREEN_HEIGHT
		define_sprites.rect.left == 0
		print 'Adding gravity'
		define_sprites.rect.bottom += constants.GRAVITY
		assert define_sprites.rect.bottom == (constants.SCREEN_HEIGHT + constants.GRAVITY)
		define_sprites.check_wall()	
		assert define_sprites.rect.bottom == constants.SCREEN_HEIGHT

	def test_coin_collision(self, define_sprites):
		#Set players initial position
		define_sprites.rect.left = 100;
		define_sprites.rect.bottom = 0;
		#Set a coin at p0layers position
		gold_coin = coin.Coin()
		gold_coin.rect.left = define_sprites.rect.left
		gold_coin.rect.bottom = define_sprites.rect.bottom
		landforms.Platform.all_coins.add(gold_coin)
		#Check coin collection
		define_sprites.collect_coin()
		assert len(landforms.Platform.all_coins) == 0
		assert define_sprites.score == 5

	def test_donkey_collision(self, define_sprites):
		#Set players initial position
		define_sprites.rect.left = 100;
		define_sprites.rect.bottom = 0;
		#Set a donkey at players position
		donkey_one = donkey.Donkey(define_sprites.rect.left, define_sprites.rect.bottom,700,950)
		define_sprites.check_donkey()
		assert define_sprites.life == constants.PLAYER_LIFE - 1
		assert define_sprites.rect.left == 0
		assert define_sprites.rect.bottom == constants.SCREEN_HEIGHT

	def test_princess_collision(self, define_sprites):
		#Set players initial position
		define_sprites.reached_princess = False
		define_sprites.rect.left = 100;
		define_sprites.rect.bottom = 0;
		define_sprites.princess.rect.left = define_sprites.rect.left
		define_sprites.princess.rect.bottom = define_sprites.rect.bottom
		define_sprites.check_princess()
		assert define_sprites.rect.left == 100
		assert define_sprites.reached_princess == False





		






