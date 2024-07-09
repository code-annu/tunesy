package developer.anurag.tunesy.main.data_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import developer.anurag.tunesy.tunesy_api.utils.MixPlaylist;

public class MixPlaylistDataModel extends ViewModel {

    private  final MutableLiveData<Map<String, List<MixPlaylist>>> mixPlaylistMap=new MutableLiveData<>();
    private Stack<String> mixPlaylistMapKeyStack=new Stack<>();
    private final MutableLiveData<List<MixPlaylist>> dailyMixPlaylistList=new MutableLiveData<>();

    public MutableLiveData<Map<String, List<MixPlaylist>>> getMixPlaylistMap() {
        return mixPlaylistMap;
    }

    public void setMixPlaylistMap(Map<String, List<MixPlaylist>> mixPlaylistMap) {
        this.mixPlaylistMap.setValue(mixPlaylistMap);
    }

    public Stack<String> getMixPlaylistMapKeyStack() {
        return mixPlaylistMapKeyStack;
    }

    public void setMixPlaylistMapKeyStack(Stack<String> mixPlaylistMapKeyStack) {
        this.mixPlaylistMapKeyStack = mixPlaylistMapKeyStack;
    }


    public MutableLiveData<List<MixPlaylist>> getDailyMixPlaylistList() {
        return dailyMixPlaylistList;
    }

    public void setDailyMixPlaylistList(List<MixPlaylist> dailyMixPlaylistList) {
        this.dailyMixPlaylistList.setValue(dailyMixPlaylistList);
    }
}
