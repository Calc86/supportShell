<?php
/**
 * Created by PhpStorm.
 * User: calc
 * Date: 02.04.14
 * Time: 20:57
 */

class Row{
    private $id;
    private $closed     = self::CLOSED_DEFAULT;
    private $cmd_accepted = self::CMD_ACCEPTED_DEFAULT;
    private $cmd_done   = self::CMD_DONE_DEFAULT;
    private $cmd        = self::CMD_DEFAULT;
    private $cmd_ret    = self::CMD_RET_DEFAULT;

    const CLOSED_DEFAULT = false;
    const CMD_ACCEPTED_DEFAULT = true;
    const CMD_DONE_DEFAULT = true;
    const CMD_DEFAULT = "";
    const CMD_RET_DEFAULT = "";

    function __construct($id)
    {
        $this->id = $id;
    }

    private function boolToString($bool){
        return ($bool) ? "true" : "false";
    }

    private function stringToBool($s){
        if($s == 'true') return true;
        else return false;
    }

    function __toString()
    {
        return  "$this->id\n".
                ($this->boolToString($this->closed))."\n".
                ($this->boolToString($this->cmd_accepted))."\n".
                ($this->boolToString($this->cmd_done))."\n".
                "$this->cmd\n".
                "$this->cmd_ret\n";
    }

    public function save(){
        if($this->id == 0)
            $this->id = "1".substr(time(),6);
        $f = fopen($this->getFilePath(),"w+");
        fwrite($f,$this);
        fclose($f);
    }

    public function setFromPost(array $post){
        $a = array('');
        foreach($post as $v) $a[] = $v;
        $this->setFromArray($a);
    }

    public function setFromArray(array $rows){
        $this->closed    = isset($rows[1]) ? $this->stringToBool($rows[1]) : self::CLOSED_DEFAULT;
        $this->cmd_accepted    = isset($rows[2]) ? $this->stringToBool($rows[2]) : self::CMD_ACCEPTED_DEFAULT;
        $this->cmd_done  = isset($rows[3]) ? $this->stringToBool($rows[3]) : self::CMD_DONE_DEFAULT;
        $this->cmd       = isset($rows[4]) ? ($rows[4]) : self::CMD_DEFAULT;
        if(isset($rows[5])){
            for($i = 5 ; $i < count($rows) ; $i++)
                $this->cmd_ret.= $rows[$i]."\n";
        }
        else{
            $this->cmd_ret = self::CMD_RET_DEFAULT;
        }
    }

    public function getFromFile($buffer){
        $rows = explode("\n", $buffer);
        $this->setFromArray($rows);
    }

    public function load(){
        if(!is_file($this->getFilePath())){
            return false;
        }
        else{
            $this->getFromFile(file_get_contents($this->getFilePath()));
            return true;
        }
    }

    /**
     * @return string
     */
    private function getFilePath()
    {
        return "./db/" . $this->id;
    }
};

$do = isset($_GET['do']) ? 1 : 0;
$id = isset($_GET['id']) ? $_GET['id'] : '';

if($id == '') die("error");

if(!$id)
{
    $row = new Row($id);
    $row->save();
    echo $row;
}
else{
    if(count($_POST)){
        $row = new Row($id);
        $row->setFromPost($_POST);
        $row->save();
        echo "OK";
        print_r($_POST);
    }
    else{
        $row = new Row($id);
        if($row->load())
            echo $row;
        else
            echo 'error';
    }
}
