// Navigation/Navigation.js

import { createAppContainer } from 'react-navigation';
import { createStackNavigator } from 'react-navigation-stack';

import SearchScreen from '../components/SearchScreen';
import LoggingScreen from '../components/LoggingScreen';



const AppNavigator = createStackNavigator(
    {
      
      Logging: {
        screen: LoggingScreen,
        navigationOptions: {
         title: 'Logging',
         headerStyle: {
          backgroundColor: '#b4815e',
        }
        }
      },
      Search: SearchScreen
    },

    {
      initialRouteName: 'Logging',
      defaultNavigationOptions: {
        headerStyle: {
          backgroundColor: '#f4511e',
        }
      }
    }
  );
  
  
  export default createAppContainer(AppNavigator);