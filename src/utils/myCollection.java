package utils;

import java.util.Comparator;

//排序函数，用于将list按照优先级从小到大快排
public class myCollection implements Comparator<PCB> {
    public int compare(PCB o1, PCB o2) {
        if (o1.getPriority() == o2.getPriority())
            return 0;
        else if (o1.getPriority() > o2.getPriority())
            return 1;
        else
            return -1;
    }
}


