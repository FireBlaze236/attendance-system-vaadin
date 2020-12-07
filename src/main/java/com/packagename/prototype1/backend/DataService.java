package com.packagename.prototype1.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataService {
    private DataRepository dataRepository;
    @Autowired
    public DataService(DataRepository dataRepository)
    {
        this.dataRepository = dataRepository;
    }

    public List<DataModel> findAll()
    {
        return dataRepository.findAll();
    }

    public long count()
    {
        return dataRepository.count();
    }

    public void delete(DataModel data)
    {
        if(data == null)
        {
            return;
        }
        dataRepository.delete(data);
    }
    public void save(DataModel data)
    {
        if(data == null)
        {
            return;
        }
        dataRepository.save(data);
    }
    public ArrayList<DataModel> findByCode(int code)
    {
        List<DataModel> fullDataList = dataRepository.findAll();
        ArrayList<DataModel> result_list = new ArrayList<DataModel>();
        for(DataModel data : fullDataList)
        {
            if(data.getCode() == code)
            {
                result_list.add(data);
            }
        }
        return result_list;
    }
}
