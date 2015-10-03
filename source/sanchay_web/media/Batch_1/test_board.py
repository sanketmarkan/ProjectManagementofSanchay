import pytest
import pygame
import constants
import board
import player
import levels
import princess

@pytest.fixture()
def define_board():
	screen = pygame.display.set_mode((constants.SCREEN_WIDTH, constants.SCREEN_HEIGHT))
	current_level = levels.LevelOne(screen)
	return current_level

@pytest.fixture()
def define_level_one():
	screen = pygame.display.set_mode((constants.SCREEN_WIDTH, constants.SCREEN_HEIGHT))
	level_one = levels.LevelOne(screen)
	return level_one

@pytest.fixture()
def define_level_two():
	screen = pygame.display.set_mode((constants.SCREEN_WIDTH, constants.SCREEN_HEIGHT))
	level_two = levels.LevelTwo(screen)
	return level_two

@pytest.fixture()
def define_level_three():
	screen = pygame.display.set_mode((constants.SCREEN_WIDTH, constants.SCREEN_HEIGHT))
	level_three = levels.LevelThree(screen)
	return level_three

def test_platform_validity(define_board):
	assert define_board.platform_zero != None
	assert define_board.platform_one != None
	assert define_board.platform_two != None
	assert define_board.platform_three != None
	assert define_board.platform_four != None

def test_ladder_validity(define_board):
	assert define_board.broken_ladder_one != None
	assert define_board.ladder_two != None
	assert define_board.ladder_three != None
	assert define_board.ladder_four != None
	assert define_board.ladder_five != None

def test_player_at_start(define_board):
	assert define_board.knight.rect.left == 0
	assert define_board.knight.rect.bottom == constants.SCREEN_HEIGHT

def test_princess_inside_cage(define_board):
	assert define_board.lady.rect.left == constants.SCREEN_HEIGHT - 100
	assert define_board.lady.rect.bottom == constants.FOUR_Y

def test_num_donkeys(define_level_one, define_level_two, define_level_three):
	assert len(define_level_one.active_sprite_list) - 2 == 1
	assert len(define_level_two.active_sprite_list) - 2 == 2
	assert len(define_level_three.active_sprite_list) - 2 == 3

# Tests that the player jumps properly
def test_player_jumps(define_board):
	#Set player on a platform
	define_board.knight.rect.left = constants.THREE_X1 + 10
	define_board.knight.rect.bottom = constants.THREE_Y
	#Give jump command to player
	define_board.knight.jump()
	define_board.knight.update()
	assert define_board.knight.rect.bottom == constants.THREE_Y - 9
	for i in range(1,100):
		define_board.knight.update()
	assert define_board.knight.rect.bottom == constants.THREE_Y



