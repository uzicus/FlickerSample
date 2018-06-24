package com.dtkachenko.sample.flickrsample.ui

import me.dmdev.rxpm.navigation.NavigationMessage

class BackMessage : NavigationMessage

class StartUpMessage : NavigationMessage

class OpenPhotoViewMessage(val photoUrl: String) : NavigationMessage